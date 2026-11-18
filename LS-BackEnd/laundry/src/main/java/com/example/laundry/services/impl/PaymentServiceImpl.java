package com.example.laundry.services.impl;

import com.example.laundry.client.CassoClient;
import com.example.laundry.dto.PaymentRequest;
import com.example.laundry.dto.PaymentResponse;
import com.example.laundry.models.order.Order;
import com.example.laundry.models.order.PaymentStatus;
import com.example.laundry.models.user.Employee;
import com.example.laundry.models.user.Roles;
import com.example.laundry.models.user.User;
import com.example.laundry.repository.EmployeeRepository;
import com.example.laundry.repository.OrderRepository;
import com.example.laundry.repository.UserRepository;
import com.example.laundry.security.SecurityUtils;
import com.example.laundry.services.PaymentService;
import com.example.laundry.utils.ApiResponse;
import com.example.laundry.utils.VietQRCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {
    private static final int POLL_PAGE_SIZE = 50;
    private static final int MAX_POLL_PAGES = 5;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    private final OrderRepository orderRepository;
    private final CassoClient cassoClient;
    private final VietQRCode vietQRCode;

    public PaymentServiceImpl(OrderRepository orderRepository, CassoClient cassoClient, VietQRCode vietQRCode) {
        this.orderRepository = orderRepository;
        this.cassoClient = cassoClient;
        this.vietQRCode = vietQRCode;
    }

    @Value("${account-holder}")
    private String accountHolder;

    @Value("${account-number}")
    private String accountNumber;

    @Value("${casso.api-key:}")
    private String cassoApiKey;

    private void verifyOrderAccess(Order order) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getName())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Yêu cầu đăng nhập");
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
        if (isAdmin) {
            return;
        }

        User currentUser = SecurityUtils.resolveCurrentUser(authentication, userRepository::findById,
                userRepository::findByUsername);
        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không tìm thấy thông tin người dùng");
        }

        if (currentUser.getRoles() == Roles.Customer) {
            if (order.getCustomer() == null || !order.getCustomer().getId().equals(currentUser.getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "Bạn không có quyền truy cập thanh toán của đơn hàng này");
            }
            return;
        }

        if (currentUser.getRoles() == Roles.StoreOwner) {
            if (order.getLaundryShop() == null || order.getLaundryShop().getStoreOwner() == null
                    || !order.getLaundryShop().getStoreOwner().getId().equals(currentUser.getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "Bạn không có quyền truy cập thanh toán của cửa hàng khác");
            }
            return;
        }

        if (currentUser.getRoles() == Roles.Employee) {
            Employee employee = employeeRepository.findById(currentUser.getId()).orElse(null);
            if (employee == null || employee.getShop() == null || order.getLaundryShop() == null
                    || !employee.getShop().getId().equals(order.getLaundryShop().getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "Bạn không có quyền truy cập thanh toán của cửa hàng khác");
            }
            return;
        }

        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Không có quyền truy cập");
    }

    @Override
    public PaymentResponse getPaymentInfo(PaymentRequest paymentRequest) {
        if (paymentRequest.getOrderId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Thiếu orderId");
        }
        Order order = orderRepository.findById(paymentRequest.getOrderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy đơn hàng!!!"));

        verifyOrderAccess(order);

        // Tạo mã qr
        String transferContent = "LAUNDRY" + order.getId();
        String bankCode = "MB";
        String totalAmount = order.getTotalAmount().toString();
        String qrCode = vietQRCode.generateQRCode(bankCode, accountNumber, totalAmount, transferContent, accountHolder);

        return PaymentResponse.builder()
                .orderId(order.getId())
                .totalAmount(order.getTotalAmount())
                .accountHolder(accountHolder)
                .bankName("MB Bank")
                .accountNumber(accountNumber)
                .transferContent(transferContent)
                .paymentStatus(order.getPaymentStatus().toString())
                .qrCode(qrCode)
                .build();
    }

    @Override
    public ApiResponse<Object> checkPaymentStatus(Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if (orderOptional.isEmpty()) {
            return new ApiResponse<>("Không tìm thấy đơn hàng với id: " + orderId);
        }

        Order order = orderOptional.get();
        verifyOrderAccess(order);

        return new ApiResponse<>("Lấy trạng thái thanh toán thành công", createPaymentStatusResponse(order));
    }

    @Override
    public void pollBankTransactions() {
        if (cassoApiKey == null || cassoApiKey.isBlank()) {
            log.debug("Skipping bank transaction polling because casso.api-key is not configured");
            return;
        }

        List<Order> pendingOrders = orderRepository.findByPaymentStatus(PaymentStatus.PENDING);

        if (pendingOrders.isEmpty()) {
            log.debug("No pending orders to check");
            return;
        }

        List<CassoClient.CassoTransaction> transactions = fetchRecentTransactions();
        if (transactions.isEmpty()) {
            return;
        }
        log.info("Fetched {} transactions from Casso API", transactions.size());

        for (Order order : pendingOrders) {
            String transferContent = "LAUNDRY" + order.getId();
            Double requiredAmount = order.getTotalAmount();

            boolean found = false;
            for (CassoClient.CassoTransaction transaction : transactions) {
                if (isMatchingTransaction(transaction, transferContent, requiredAmount)) {
                    order.setPaymentStatus(PaymentStatus.PAID);
                    orderRepository.save(order);
                    found = true;
                    break;
                }
            }

            if (!found) {
                log.debug("No matching transaction found for order: {}", order.getId());
            }
        }
    }

    private List<CassoClient.CassoTransaction> fetchRecentTransactions() {
        List<CassoClient.CassoTransaction> allTransactions = new ArrayList<>();
        for (int page = 1; page <= MAX_POLL_PAGES; page++) {
            CassoClient.CassoTransactionResponse response = cassoClient.getTransactions(page, POLL_PAGE_SIZE);
            if (response == null || response.getData() == null || response.getData().getRecords() == null) {
                if (page == 1) {
                    log.warn("Failed to get transactions from Casso API");
                }
                break;
            }
            List<CassoClient.CassoTransaction> records = response.getData().getRecords();
            if (records.isEmpty()) {
                break;
            }
            allTransactions.addAll(records);
            if (records.size() < POLL_PAGE_SIZE) {
                break;
            }
        }
        return allTransactions;
    }

    private boolean isMatchingTransaction(CassoClient.CassoTransaction transaction, String transferContent,
            Double requiredAmount) {
        if (transaction == null || transferContent == null || transferContent.isBlank()
                || requiredAmount == null || transaction.getAmount() == null) {
            return false;
        }

        // Kiểm tra mã đơn hàng với ranh giới rõ ràng để tránh khớp nhầm (VD: LAUNDRY1
        // vs LAUNDRY10)
        String description = transaction.getDescription();
        if (description == null) {
            return false;
        }
        Pattern codePattern = Pattern.compile("(?i)(?<![a-z0-9])" + Pattern.quote(transferContent.trim()) + "(?!\\d)");
        if (!codePattern.matcher(description).find()) {
            return false;
        }

        // Kiểm tra số tiền
        if (transaction.getAmount() < requiredAmount) {
            return false;
        }

        // Kiểm tra transaction gần đây (dưới 24 hours)
        if (transaction.getTransactionTime() == null ||
                transaction.getTransactionTime().isBefore(LocalDateTime.now().minusHours(24))) {
            log.debug("Ignoring stale or timestamp-less transaction: {}", transaction.getDescription());
            return false;
        }

        return true;
    }

    private Map<String, Object> createPaymentStatusResponse(Order order) {
        Map<String, Object> response = new HashMap<>();
        response.put("orderId", order.getId());
        response.put("totalAmount", order.getTotalAmount());
        response.put("orderStatus", order.getOrderStatus());
        response.put("paymentStatus", order.getPaymentStatus());
        response.put("createdAt", order.getCreatedAt());

        return response;
    }
}
