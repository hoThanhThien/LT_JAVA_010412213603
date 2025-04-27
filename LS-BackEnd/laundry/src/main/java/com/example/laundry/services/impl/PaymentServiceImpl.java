package com.example.laundry.services.impl;

import com.example.laundry.client.CassoClient;
import com.example.laundry.dto.PaymentRequest;
import com.example.laundry.dto.PaymentResponse;
import com.example.laundry.models.order.Order;
import com.example.laundry.models.order.PaymentStatus;
import com.example.laundry.repository.OrderRepository;
import com.example.laundry.repository.PaymentRepository;
import com.example.laundry.services.PaymentService;
import com.example.laundry.utils.ApiResponse;
import com.example.laundry.utils.VietQRCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    private final OrderRepository orderRepository;
    private final CassoClient cassoClient;
    @Autowired
    private VietQRCode vietQRCode;

    public PaymentServiceImpl(OrderRepository orderRepository, CassoClient cassoClient, VietQRCode vietQRCode) {
            this.orderRepository = orderRepository;
            this.cassoClient = cassoClient;
            this.vietQRCode = vietQRCode;
      }

    @Value("${account-holder}")
    private String accountHolder;

    @Value("${account-number}")
    private String accountNumber;

    @Override
    public PaymentResponse getPaymentInfo(PaymentRequest paymentRequest) {
        Order order = orderRepository.findById(paymentRequest.getOrderId().intValue())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng!!!"));


        //Tạo mã qr
        String transferContent = "LAUNDRY" + order.getId() + accountHolder + accountHolder;
        String bankCode = "MB";
        String totalAmount = order.getTotalAmount().toString();
        String qrCode = vietQRCode.generateQRCode(bankCode, accountNumber, totalAmount, transferContent, accountHolder);

        return PaymentResponse.builder()
                .orderId(Long.valueOf(order.getId().toString()))
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
        Optional<Order> orderOptional = orderRepository.findById(orderId.intValue());

        if (orderOptional.isEmpty()) {
            return new ApiResponse<>("Không tìm thấy đơn hàng với id: " + orderId);
        }

        Order order = orderOptional.get();

        return new ApiResponse<>("Lấy trạng thái thanh toán thành công", createPaymentStatusResponse(order));
    }

    @Override
    public void pollBankTransactions() {

        List<Order> pendingOrders = orderRepository.findByPaymentStatus(PaymentStatus.PENDING);

        if (pendingOrders.isEmpty()) {
            System.out.println("No pending orders to check");
            return;
        }

        CassoClient.CassoTransactionResponse response = cassoClient.getTransactions(1, 20);

        if (response == null || response.getData() == null || response.getData().getRecords() == null) {
            System.out.println("Failed to get transactions from Casso API");
            return;
        }

        List<CassoClient.CassoTransaction> transactions = response.getData().getRecords();
        System.out.printf("Fetched {} transactions from Casso API", transactions.size());

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
                System.out.printf("No matching transaction found for order: {}", order.getId());
            }
        }
    }

    private boolean isMatchingTransaction(CassoClient.CassoTransaction transaction, String transferContent, Double requiredAmount) {
        // Kiểm tra xem nội dung chuyển khoản của cus có thuộc nội dung chuyển khoản yêu cầu không
        String description = transaction.getDescription();
        if (description == null || !description.toLowerCase().contains(transferContent.toLowerCase().trim())) {
            return false;
        }

        // Kiểm tra số tiền
        if (transaction.getAmount() < requiredAmount) {
            return false;
        }

        // Kiểm tra transaction gần đây (dưới 24 hours)
        if (transaction.getTransactionTime() == null ||
                transaction.getTransactionTime().isBefore(LocalDateTime.now().minusHours(24))) {
            System.out.println("Checking transaction: " + transaction.getDescription());
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
