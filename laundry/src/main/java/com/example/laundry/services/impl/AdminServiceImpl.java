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
        // Kiểm tra dữ liệu
        if (storeOwnerDTO.getPassword() == null || storeOwnerDTO.getPassword().isEmpty()) {
            return new ApiResponse<>("Password không được để trống", null);
        }

        //Kiểm tra password
        if(!UserValidator.isValidPassword(storeOwnerDTO.getPassword())){
            return new ApiResponse<>("Password không hợp lệ!!!", null);
        }

        //Kiểm tra email
        if(!UserValidator.isValidEmail(storeOwnerDTO.getEmail())){
            return new ApiResponse<>("Email không hợp lệ!!!", null);
        }
        if(storeOwnerRepository.existsByEmail(storeOwnerDTO.getEmail())){
            return new ApiResponse<>("Email đã tồn tại!!!", null);
        }

        //Kiểm tra phone
        if(!UserValidator.isValidPhone(storeOwnerDTO.getPhone())){
            return new ApiResponse<>("Phone phải có 10 chữ số!!!", null);
        }
        if(storeOwnerRepository.existsByPhone(storeOwnerDTO.getPhone())){
            return new ApiResponse<>("Phone đã tồn tại!!!", null);
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

        return new ApiResponse<>("Tạo tài khoản Store Owner thành công", storeOwner);
    }

    @Override
    public ApiResponse<String> removeStoreOwner(StoreOwnerDTO storeOwnerDTO) {
        if (storeOwnerDTO.getEmail() == null && storeOwnerDTO.getPhone() == null && storeOwnerDTO.getUsername() == null) {
            return new ApiResponse<>("Không đủ thông tin để xóa!!!", null);
        }

        StoreOwner storeOwner = findStoreOwnerByInfo(storeOwnerDTO);

        if (storeOwner == null) {
            return new ApiResponse<>("Không tìm thấy Store Owner với thông tin đã cung cấp", null);
        }

        if (!storeOwnerRepository.existsById(storeOwner.getId())) {
            return new ApiResponse<>("Store Owner không tồn tại hoặc đã bị xóa trước đó", null);
        }

        storeOwnerService.deleteStoreOwner(storeOwner);

        return new ApiResponse<>("Đã xóa Store Owner thành công", null);
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
