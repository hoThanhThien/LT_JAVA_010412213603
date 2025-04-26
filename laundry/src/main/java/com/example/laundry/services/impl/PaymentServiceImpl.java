package com.example.laundry.services.impl;

import com.example.laundry.client.CassoClient;
import com.example.laundry.dto.PaymentConfirmRequest;
import com.example.laundry.dto.PaymentRequest;
import com.example.laundry.dto.PaymentResponse;
import com.example.laundry.models.order.Order;
import com.example.laundry.models.order.Payment;
import com.example.laundry.models.order.PaymentStatus;
import com.example.laundry.repository.OrderRepository;
import com.example.laundry.repository.PaymentRepository;
import com.example.laundry.services.PaymentService;
import com.example.laundry.utils.ApiResponse;
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

    public PaymentServiceImpl(OrderRepository orderRepository, CassoClient cassoClient) {
        this.orderRepository = orderRepository;
        this.cassoClient = cassoClient;
    }

    @Value("${account-holder}")
    private String accountHolder;

    @Value("${account-number}")
    private String accountNumber;

    @Override
    public PaymentResponse getPaymentInfo(PaymentRequest paymentRequest) {
        Order order = orderRepository.findById(paymentRequest.getOrderId().intValue())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng!!!"));

        //Tạo nội dung chuyển khoản
        String transferContent = "LAUNDRY" + order.getId();

        return PaymentResponse.builder()
                .orderId(Long.valueOf(order.getId().toString()))
                .totalAmount(order.getTotalAmount())
                .accountHolder(accountHolder)
                .bankName("MB Bank")
                .accountNumber(accountNumber)
                .transferContent(transferContent)
                .paymentStatus(order.getPaymentStatus().toString())
                .build();
    }

    @Override
    public ApiResponse<Object> confirmPayment(PaymentConfirmRequest request) {
        //Tìm đơn hàng theo id
        Optional<Order> orderOptional = orderRepository.findById(request.getOrderId().intValue());

        if (orderOptional.isEmpty()) {
            return new ApiResponse<>("Không tìm thấy đơn hàng với id: " + request.getOrderId());
        }

        Order order = orderOptional.get();

        //Kiểm tra nếu đơn hàng được thanh toán rồi
        if(PaymentStatus.PAID.equals(order.getPaymentStatus())) {
            return new ApiResponse<>("Đơn hàng đã được thanh toán",
                    createPaymentStatusResponse(order));
        }

        order.setPaymentStatus(PaymentStatus.PAID);
        orderRepository.save(order);

        return new ApiResponse<>("Xác nhận thanh toán thành công", createPaymentStatusResponse(order));
    }

    @Override
    public ApiResponse<Object> checkPaymentStatus(Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId.intValue());

        if(orderOptional.isEmpty()) {
            return new ApiResponse<>("Không tìm thấy đơn hàng với id: "  + orderId);
        }

        Order order = orderOptional.get();

        if (PaymentStatus.PENDING.equals(order.getPaymentStatus())) {
            verifyPaymentFromBank(orderId);
            order = orderRepository.findById(orderId.intValue()).get();
        }

        return new ApiResponse<>("Lấy trạng thái thanh toán thành công", createPaymentStatusResponse(order));
    }

    @Override
    public ApiResponse<Object> verifyPaymentFromBank(Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId.intValue());

        if (orderOptional.isEmpty()) {
            return new ApiResponse<>("Không tìm thấy đơn hàng với id: " + orderId);
        }

        Order order = orderOptional.get();

        if (PaymentStatus.PAID.equals(order.getPaymentStatus())) {
            return new ApiResponse<>("Đơn hàng đã được thanh toán", createPaymentStatusResponse(order));
        }

        String transferContent = "LAUNDRY" + order.getId();
        Double requiredAmount = order.getTotalAmount();

        boolean paymentFound = findMatchingTransaction(transferContent, requiredAmount);

        if (paymentFound) {
            order.setPaymentStatus(PaymentStatus.PAID);
            orderRepository.save(order);
            System.out.printf("Đã tìm thấy giao dịch cho đơn hàng: {}", orderId);
            return new ApiResponse<>("Thanh toán thành công", createPaymentStatusResponse(order));
        }

        return new ApiResponse<>("Chưa tìm thấy giao dịch phù hợp", createPaymentStatusResponse(order));
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

    private boolean findMatchingTransaction(String transferContent, Double requiredAmount) {
        CassoClient.CassoTransactionResponse response = cassoClient.getTransactions(1, 20);

        if (response == null || response.getData() == null || response.getData().getRecords() == null) {
            System.out.println("Failed to get transactions from Casso API");
            return false;
        }

        List<CassoClient.CassoTransaction> transactions = response.getData().getRecords();

        // Look for a transaction that matches our criteria
        for (CassoClient.CassoTransaction transaction : transactions) {
            if (isMatchingTransaction(transaction, transferContent, requiredAmount)) {
                return true;
            }
        }

        return false;
    }

    private boolean isMatchingTransaction(CassoClient.CassoTransaction transaction, String transferContent, Double requiredAmount) {
        // Check if transaction description contains our transfer content
        String description = transaction.getDescription();
        if (description == null || !description.toLowerCase().contains(transferContent.toLowerCase().trim())) {
            return false;
        }

        // Check if amount matches or exceeds required amount
        if (transaction.getAmount() < requiredAmount) {
            return false;
        }

        // Check if transaction is recent (within last 24 hours)
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
