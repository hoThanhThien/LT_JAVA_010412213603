package com.example.laundry.controllers;

import com.example.laundry.dto.StoreOwnerDTO;
import com.example.laundry.models.user.StoreOwner;
import com.example.laundry.models.user.Roles;
import com.example.laundry.repository.StoreOwnerRepository;
import com.example.laundry.services.StoreOwnerService;
import com.example.laundry.utils.UserValidator;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private StoreOwnerService storeOwnerService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StoreOwnerRepository storeOwnerRepository;

    @PostMapping("/storeowner/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createStoreOwner(@RequestBody StoreOwnerDTO storeOwnerDTO){
        // Kiểm tra dữ liệu
        if (storeOwnerDTO.getPassword() == null || storeOwnerDTO.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body("Password không được để trống");
        }

        //Kiểm tra password
        if(!UserValidator.isValidPassword(storeOwnerDTO.getPassword())){
            return ResponseEntity.badRequest().body("Password không hợp lệ!!!");
        }

        //Kiểm tra email
        if(!UserValidator.isValidEmail(storeOwnerDTO.getEmail())){
            return ResponseEntity.badRequest().body("Email không hợp lệ!!!");
        }
        if(storeOwnerRepository.existsByEmail(storeOwnerDTO.getEmail())){
            return ResponseEntity.badRequest().body("Email đã tồn tại!!!");
        }

        //Kiểm tra phone
        if(!UserValidator.isValidPhone(storeOwnerDTO.getPhone())){
            return ResponseEntity.badRequest().body("Phone phải có 10 chữ số!!!");
        }
        if(storeOwnerRepository.existsByPhone(storeOwnerDTO.getPhone())){
            return ResponseEntity.badRequest().body("Phone đã tồn tại!!!");
        }

        // Mã hóa password trước khi lưu vào DB
        String encodedPassword = passwordEncoder.encode(storeOwnerDTO.getPassword());

        StoreOwner storeOwner = new StoreOwner(
                storeOwnerDTO.getUsername(),
                encodedPassword,
                storeOwnerDTO.getEmail(),
                storeOwnerDTO.getPhone(),
                storeOwnerDTO.getAddress(),
                Roles.StoreOwner
        );

        storeOwnerService.addStoreOwner(storeOwner);

        return ResponseEntity.ok("Tạo tài khoản Store Owner thành công");
    }
}
