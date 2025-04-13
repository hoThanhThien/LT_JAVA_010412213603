package com.example.laundry.services.impl;

import com.example.laundry.dto.CustomerResponseDTO;
import com.example.laundry.dto.RegisterRequest;
import com.example.laundry.dto.RegisterResponse;
import com.example.laundry.models.notification.RefreshToken;
import com.example.laundry.models.user.Customer;
import com.example.laundry.models.user.Roles;
import com.example.laundry.repository.CustomerRepository;
import com.example.laundry.repository.UserRepository;
import com.example.laundry.security.JwtUtil;
import com.example.laundry.services.CustomerService;
import com.example.laundry.services.RefreshTokenService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@org.springframework.stereotype.Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
  @Autowired
  private JwtUtil jwtUtil;
  @Autowired
  private RefreshTokenService refreshTokenService;

  @Override
    public Customer addCustomer(Customer customer) {
        customerRepository.save(customer);
        return customer;
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FirebaseAuth firebaseAuth;

    @Override
    @Transactional
    public CustomerResponseDTO register(RegisterRequest registerRequest) {
        // Xác minh idToken từ firebase để lấy số điện thoại
        try {
            FirebaseToken decodedToken = firebaseAuth.verifyIdToken(registerRequest.getIdToken());
            String verifiedPhone = null;

            Map<String, Object> claims = decodedToken.getClaims();

            if (claims.containsKey("phone_number")) {
                verifiedPhone = (String) claims.get("phone_number");

                //Chuyển +84 thành 0
                if(verifiedPhone.startsWith("+84")) {
                    verifiedPhone = "0" + verifiedPhone.substring(3);
                }
            }

            //Kiểm tra xem có dữ liệu chưa
            if (verifiedPhone == null || verifiedPhone.isEmpty()) {
                return new CustomerResponseDTO("Số điện thoại không tồn tại", null);
            }

            //Kiểm tra tồn tại
            if(customerRepository.findByPhone(verifiedPhone).isPresent()) {
                System.out.println("Số điện thoại đã tồn tại trong hệ thống: " + verifiedPhone);
                return new CustomerResponseDTO("Số điện thoại đã được đăng ký trong hệ thống", null);
            }

            //Tạo người dùng
            Customer customer = new Customer();
            customer.setPhone(verifiedPhone);
            customer.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            customer.setUsername(registerRequest.getUsername());
            customer.setRoles(Roles.Customer);

            Customer savedCustomer = customerRepository.save(customer);
            //Tạo access token
            String accessToken = jwtUtil.generateAccessToken(savedCustomer.getUsername());

            //Tạo refresh token
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(savedCustomer);
            String refreshTokenString = refreshToken.getToken();

            CustomerResponseDTO.AccountInfo accountInfor = new CustomerResponseDTO.AccountInfo(
                    savedCustomer.getId(),
                    savedCustomer.getUsername(),
                    savedCustomer.getEmail(),
                    savedCustomer.getPhone(),
                    savedCustomer.getAddress(),
                    Roles.Customer
            );

            CustomerResponseDTO.DataInfo dataInfo = new CustomerResponseDTO.DataInfo(
                    accessToken,
                    refreshTokenString,
                    accountInfor
            );

            return new CustomerResponseDTO("Đăng ký thành công!!!",  dataInfo);
        } catch (FirebaseAuthException e) {
            System.err.println("Firebase authentication error: " + e.getMessage());
            return new CustomerResponseDTO("Xác minh Firebase thất bại: " + e.getMessage(), null);
        } catch (Exception e) {
            System.err.println("Registration error: " + e.getMessage());
            e.printStackTrace();
            return new CustomerResponseDTO("Đăng ký thất bại: " + e.getMessage(), null);
        }
    }

//    @Override
//    public Order bookOrder(Customer customer, LaundryShop laundryShop, Service service, String instructions) {
//        return customerRepository.bookOrder(customer, laundryShop, service);
//    }
//
//    @Override
//    public void trackOrder(Customer customer, Order order) {
//
//    }
//
//    @Override
//    public void makePayment(Customer customer, Order order, String paymentMethod, double amount) {
//
//    }
//
//    @Override
//    public List<Order> getOrderHistory(Customer customer) {
//        return null;
//    }
//
//    @Override
//    public List<LaundryShop> searchShops(Customer customer, String location) {
//        return null;
//    }
}
