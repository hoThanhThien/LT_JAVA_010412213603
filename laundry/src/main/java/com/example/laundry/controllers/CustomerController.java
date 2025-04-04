package com.example.laundry.controllers;

import com.example.laundry.dto.CustomerDTO;
import com.example.laundry.dto.CustomerResponseDTO;
import com.example.laundry.models.user.Customer;
import com.example.laundry.models.user.Roles;
import com.example.laundry.notification.EmailService;
import com.example.laundry.repository.CustomerRepository;
import com.example.laundry.services.CustomerService;
import com.example.laundry.utils.ApiResponse;
import com.example.laundry.utils.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> createCustomer(@RequestBody CustomerDTO customerDTO){
        //Kiểm tra dữ liệu
        if(customerDTO.getPassword() == null || customerDTO.getPassword().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Password không được để trống", null));
        }

        //Kiểm tra password
        if(!UserValidator.isValidPassword(customerDTO.getPassword())) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Password không hợp lệ!!!", null));
        }

        //Kiểm tra email
        if(!UserValidator.isValidEmail(customerDTO.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Email không hợp lệ!!!", null));
        }

        if(customerRepository.existsByEmail(customerDTO.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Email đã tồn tại!!!", null));
        }

        //Kiểm tra phone
        if(!UserValidator.isValidPhone(customerDTO.getPhone())) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Phone phải có 10 chữ số!!!", null));
        }

        if(customerRepository.existsByPhone(customerDTO.getPhone())) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Phone đã tồn tại!!!", null));
        }

        //Mã hóa password trước khi lưu xuống db
        String encodedPassword = passwordEncoder.encode(customerDTO.getPassword());

        Customer customer = new Customer();
        customer.setUsername(customerDTO.getUsername());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhone(customerDTO.getPhone());
        customer.setAddress(customerDTO.getAddress());
        customer.setRoles(Roles.Customer);
        customer.setPassword(encodedPassword);
        customer.setPassword(encodedPassword);

        // Tạo mã xác thực
        String verificationToken = UUID.randomUUID().toString();
        customer.setVerificationToken(verificationToken);
        customer.setEmailVerified(false);

        Customer savedCustomer = customerService.addCustomer(customer);

        CustomerResponseDTO responseDTO = new CustomerResponseDTO(
                savedCustomer.getId(),
                savedCustomer.getUsername(),
                savedCustomer.getEmail(),
                savedCustomer.getPhone(),
                savedCustomer.getAddress(),
                Roles.Customer
        );

        //Gửi email xác thực
        String verificationLink = String.format("http://localhost:8080/auth/verify?token=" + verificationToken);
        emailService.sendVerificationEmail(customer.getEmail(), verificationLink);

        return ResponseEntity
                .ok(new ApiResponse<>("Tạo tài khoản khách hàng thành công. Vui lòng kiểm tra email để xác thực!!!", responseDTO));
    }

    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<String>> verifyEmail(@RequestParam("token") String token){
        Customer customer =  customerRepository.findByVerificationToken(token);
        if(customer != null) {
            customer.setEmailVerified(true);
            customer.setVerificationToken(null); //Xóa mã xác thực sau khi xác thực
            customerRepository.save(customer);
            return ResponseEntity
                    .ok(new ApiResponse<>("Email đã xác thực thành công!!!", null));
        }
        return ResponseEntity.badRequest()
                .body(new ApiResponse<>("Mã xác thực không hơpj lệ!!!", null));
    }
}
