package com.example.laundry.controllers;

import com.example.laundry.dto.StoreOwnerDTO;
import com.example.laundry.models.user.StoreOwner;
import com.example.laundry.models.user.Roles;
import com.example.laundry.services.StoreOwnerService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private StoreOwnerService storeOwnerService;

    @PostMapping("/storeowner/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String createStoreOwner(StoreOwnerDTO storeOwnerDTO){
        StoreOwner storeOwner = new StoreOwner(
                storeOwnerDTO.getUsername(),
                storeOwnerDTO.getPassword(),
                storeOwnerDTO.getEmail(),
                storeOwnerDTO.getPhone(),
                storeOwnerDTO.getAddress(),
                Roles.StoreOwner
        );

        storeOwnerService.addStoreOwner(storeOwner);

        return "redirect:/admin";
    }
}
