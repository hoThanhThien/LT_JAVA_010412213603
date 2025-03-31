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
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/storeowner/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteStoreOwner(@RequestBody StoreOwnerDTO storeOwnerDTO){
        //kiểm tra xem có tồn tại không
        if (storeOwnerDTO.getEmail() == null && storeOwnerDTO.getPhone() == null && storeOwnerDTO.getUsername() == null && storeOwnerDTO.getPassword() == null) {
            return ResponseEntity.badRequest().body("Không đủ thông tin để xóa!!!");
        }

        // Get data
        StoreOwner storeOwner = null;
        if(storeOwnerDTO.getEmail() != null){
            storeOwner = storeOwnerRepository.findByEmail(storeOwnerDTO.getEmail());
        }
        if(storeOwner == null &&  storeOwnerDTO.getPhone() != null){
            storeOwner = storeOwnerRepository.findByPhone(storeOwnerDTO.getPhone());
        }
        if(storeOwner == null &&  storeOwnerDTO.getUsername() != null){
            storeOwner = storeOwnerRepository.findByUsername(storeOwnerDTO.getUsername());
        }

        if (storeOwner == null) {
            return ResponseEntity.badRequest().body("Không tìm thấy Store Owner với thông tin đã cung cấp");
        }

        if(!storeOwnerRepository.existsById(storeOwner.getId())){
            return ResponseEntity.badRequest().body("Store Owner không tồn tại hoặc đã bị xóa trước đó");
        }

        storeOwnerService.deleteStoreOwner(storeOwner);

        return ResponseEntity.ok("Đã xóa Store Owner thành công");
    }
}
