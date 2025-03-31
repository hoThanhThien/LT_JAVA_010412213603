package com.example.laundry.controllers;

import com.example.laundry.dto.CustomerDTO;
import com.example.laundry.models.user.Customer;
import com.example.laundry.models.user.Roles;
import com.example.laundry.notification.EmailService;
import com.example.laundry.repository.CustomerRepository;
import com.example.laundry.services.CustomerService;
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

    @PostMapping("/create")
    public ResponseEntity<String> createCustomer(@RequestBody CustomerDTO customerDTO){
        //Kiểm tra dữ liệu
        if(customerDTO.getPassword() == null || customerDTO.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body("Password không được để trống");
        }

        //Kiểm tra password
        if(!UserValidator.isValidPassword(customerDTO.getPassword())) {
            return ResponseEntity.badRequest().body("Password không hợp lệ!!!");
        }

        //Kiểm tra email
        if(!UserValidator.isValidEmail(customerDTO.getEmail())) {
            return ResponseEntity.badRequest().body("Email không hợp lệ!!!");
        }

        if(customerRepository.existsByEmail(customerDTO.getEmail())) {
            return ResponseEntity.badRequest().body("Email đã tồn tại!!!");
        }

        //Kiểm tra phone
        if(!UserValidator.isValidPhone(customerDTO.getPhone())) {
            return ResponseEntity.badRequest().body("Phone phải có 10 chữ số!!!");
        }

        if(customerRepository.existsByPhone(customerDTO.getPhone())) {
            return ResponseEntity.badRequest().body("Phone đã tồn tại!!!");
        }

        //Mã hóa password trước khi lưu xuống db
        String encodedPassword = passwordEncoder.encode(customerDTO.getPassword());

        Customer customer = new Customer(
                customerDTO.getUsername(),
                encodedPassword,
                customerDTO.getEmail(),
                customerDTO.getPhone(),
                customerDTO.getAddress(),
                Roles.Customer
        );

        // Tạo mã xác thực
        String verificationToken = UUID.randomUUID().toString();
        customer.setVerificationToken(verificationToken);
        customer.setEmailVerified(false);

        customerService.addCustomer(customer);

        //Gửi email xác thực
        String verificationLink = String.format("http://localhost:8080/auth/verify?token=" + verificationToken);
        emailService.sendVerificationEmail(customer.getEmail(), verificationLink);

        return ResponseEntity.ok("Tạo tài khoản customer thành công. Vui lòng kiểm tra email để xác thực!!");
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token){
        Customer customer =  customerRepository.findByVerificationToken(token);
        if(customer != null) {
            customer.setEmailVerified(true);
            customer.setVerificationToken(null); //Xóa mã xác thực sau khi xác thực
            customerRepository.save(customer);
            return ResponseEntity.ok("Email xác thực thành công!!!");
        }
        return ResponseEntity.badRequest().body("Mã xác thực không hợp lệ!!!");
    }
}
