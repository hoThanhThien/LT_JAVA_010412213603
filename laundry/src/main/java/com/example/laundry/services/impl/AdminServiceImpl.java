package com.example.laundry.services.impl;

import com.example.laundry.dto.StoreOwnerDTO;
import com.example.laundry.models.user.Roles;
import com.example.laundry.models.user.StoreOwner;
import com.example.laundry.repository.AdminRepository;
import com.example.laundry.repository.StoreOwnerRepository;
import com.example.laundry.services.AdminService;
import com.example.laundry.services.StoreOwnerService;
import com.example.laundry.utils.ApiResponse;
import com.example.laundry.utils.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private StoreOwnerRepository storeOwnerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private StoreOwnerService storeOwnerService;

    @Override
    public ApiResponse<StoreOwner> createStoreOwner(StoreOwnerDTO storeOwnerDTO) {
        // Validate data
        if (storeOwnerDTO.getPassword() == null || storeOwnerDTO.getPassword().isEmpty()) {
            return ApiResponse.error("Password không được để trống");
        }

        // Validate password
        if(!UserValidator.isValidPassword(storeOwnerDTO.getPassword())){
            return ApiResponse.error("Password không hợp lệ!!!");
        }

        // Validate email
        if(!UserValidator.isValidEmail(storeOwnerDTO.getEmail())){
            return ApiResponse.error("Email không hợp lệ!!!");
        }
        if(storeOwnerRepository.existsByEmail(storeOwnerDTO.getEmail())){
            return ApiResponse.error("Email đã tồn tại!!!");
        }

        // Validate phone
        if(!UserValidator.isValidPhone(storeOwnerDTO.getPhone())){
            return ApiResponse.error("Phone phải có 10 chữ số!!!");
        }
        if(storeOwnerRepository.existsByPhone(storeOwnerDTO.getPhone())){
            return ApiResponse.error("Phone đã tồn tại!!!");
        }

        // Encode password before saving to DB
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

        return ApiResponse.success(storeOwner, "Tạo tài khoản Store Owner thành công");
    }

    @Override
    public ApiResponse<String> removeStoreOwner(StoreOwnerDTO storeOwnerDTO) {
        if (storeOwnerDTO.getEmail() == null && storeOwnerDTO.getPhone() == null && storeOwnerDTO.getUsername() == null) {
            return ApiResponse.error("Không đủ thông tin để xóa!!!");
        }

        StoreOwner storeOwner = findStoreOwnerByInfo(storeOwnerDTO);

        if (storeOwner == null) {
            return ApiResponse.error("Không tìm thấy Store Owner với thông tin đã cung cấp");
        }

        if (!storeOwnerRepository.existsById(storeOwner.getId())) {
            return ApiResponse.error("Store Owner không tồn tại hoặc đã bị xóa trước đó");
        }

        storeOwnerService.deleteStoreOwner(storeOwner);

        return ApiResponse.success("Đã xóa Store Owner thành công");
    }

    private StoreOwner findStoreOwnerByInfo(StoreOwnerDTO storeOwnerDTO) {
        StoreOwner storeOwner = null;
        if (storeOwnerDTO.getEmail() != null) {
            storeOwner = storeOwnerRepository.findByEmail(storeOwnerDTO.getEmail());
        }
        if (storeOwner == null && storeOwnerDTO.getPhone() != null) {
            storeOwner = storeOwnerRepository.findByPhone(storeOwnerDTO.getPhone());
        }
        if (storeOwner == null && storeOwnerDTO.getUsername() != null) {
            storeOwner = storeOwnerRepository.findByUsername(storeOwnerDTO.getUsername());
        }
        return storeOwner;
    }
}