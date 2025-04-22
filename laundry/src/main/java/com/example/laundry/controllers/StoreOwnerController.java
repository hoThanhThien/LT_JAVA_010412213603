package com.example.laundry.controllers;

import com.example.laundry.dto.*;
import com.example.laundry.models.shop.Service;
import com.example.laundry.models.user.Employee;
import com.example.laundry.models.user.StoreOwner;
import com.example.laundry.repository.StoreOwnerRepository;
import com.example.laundry.services.EmployeeService;
import com.example.laundry.services.StoreOwnerService;
import com.example.laundry.utils.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://127.0.0.1:3000", allowCredentials = "true")
@RestController
@RequestMapping("/storeowner")
public class StoreOwnerController {
    @Autowired
    private StoreOwnerService storeOwnerService;

    @Autowired
    private StoreOwnerRepository storeOwnerRepository;
  @Autowired
  private EmployeeService employeeService;

    // Lấy store owner hiện tại
    private StoreOwner getCurrentStoreOwner() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return storeOwnerRepository.findByUsername(currentUsername);
    }

    @PostMapping("/employee/create")
    @PreAuthorize("hasRole('STOREOWNER')")
    public ResponseEntity<ApiResponse<EmployeeDTO>> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        // Lấy thông tin StoreOwner hiện tại
        StoreOwner storeOwner = getCurrentStoreOwner();
        if (storeOwner == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Không tìm thấy thông tin StoreOwner", null));
        }

        ApiResponse<Employee> response = storeOwnerService.createEmployee(storeOwner, employeeDTO);

        if (response.getData() == null) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(response.getMessage(), null));
        }

        EmployeeDTO responseDTO = new EmployeeDTO(response.getData());

        return ResponseEntity.ok(new ApiResponse<>("Tạo nhân viên thành công", responseDTO));
    }

    @DeleteMapping("/employee/delete")
    @PreAuthorize("hasRole('STOREOWNER')")
    public ResponseEntity<ApiResponse<String>> deleteEmployee(@RequestBody EmployeeDTO employeeDTO) {
        // Lấy thông tin StoreOwner hiện tại
        StoreOwner storeOwner = getCurrentStoreOwner();
        if (storeOwner == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Không tìm thấy thông tin StoreOwner", null));
        }
        ApiResponse<String> response = storeOwnerService.deleteEmployee(storeOwner, employeeDTO);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/employee/update")
    @PreAuthorize("hasRole('STOREOWNER')")
    public ResponseEntity<ApiResponse<EmployeeDTO>> updateEmployee(@RequestBody EmployeeDTO employeeDTO) {
        StoreOwner storeOwner = getCurrentStoreOwner();
        if (storeOwner == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Không tìm thấy thông tin StoreOwner", null));
        }

        ApiResponse<EmployeeDTO> responseDTO = storeOwnerService.updateEmployee(storeOwner, employeeDTO);

        if (responseDTO.getData() == null) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(responseDTO.getMessage(), null));
        }

        return ResponseEntity.ok(new ApiResponse<>("Cập nhật thông tin nhân viên thành công", responseDTO).getData());
    }

    @GetMapping("/employees")
    @PreAuthorize("hasRole('STOREOWNER')")
    public ResponseEntity<PagedResponse<EmployeeDTO>> getMyEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            StoreOwner storeOwner = getCurrentStoreOwner();
            PagedResponse<EmployeeDTO> response = employeeService.getAllEmployeesByStoreOwner(storeOwner, page, size);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new PagedResponse<>("Lấy danh sách nhân viên thất bại: " + e.getMessage(), null));
        }
    }

    @PostMapping("/shop/create")
    @PreAuthorize("hasRole('STOREOWNER')")
    public ResponseEntity<ApiResponse<LaundryShopDTO>> createShop(@RequestBody LaundryShopDTO laundryShopDTO) {
        //Lấy thông tin storeowner hiện tại
        StoreOwner storeOwner = getCurrentStoreOwner();
        if (storeOwner == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Không tìm thấy thông tin storeowner", null));
        }

        ApiResponse<LaundryShopDTO> response = storeOwnerService.createShop(storeOwner, laundryShopDTO);

        if (response.getData() == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/shop/update")
    @PreAuthorize("hasRole('STOREOWNER')")
    public ResponseEntity<ApiResponse<LaundryShopDTO>> updateShop(@RequestBody LaundryShopDTO laundryShopDTO) {
        //Kiểm tra thông tin storeowner
        StoreOwner storeOwner = getCurrentStoreOwner();
        if (storeOwner == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Không tìm thấy thông tin storeowner"));
        }

        ApiResponse<LaundryShopDTO> response = storeOwnerService.updateLaundryShop(storeOwner, laundryShopDTO);

        if(response.getData() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/shop/delete")
    @PreAuthorize("hasRole('STOREOWNER')")
    public ResponseEntity<ApiResponse<String>> deleteShop(@RequestBody LaundryShopDTO laundryShopDTO) {
        //Kiểm tra thông tin storeowner
        StoreOwner storeOwner = getCurrentStoreOwner();
        if (storeOwner == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Không tìm thấy thông tin storeowner"));
        }

        ApiResponse<String> response = storeOwnerService.deleteLaundryShop(storeOwner, laundryShopDTO);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/category/add")
    @PreAuthorize("hasRole('STOREOWNER')")
    public ResponseEntity<ApiResponse<ServiceCategoryDTO>> addServiceCategory(@RequestBody ServiceCategoryDTO serviceCategoryDTO) {
        //Kiểm tra thông tin storeowner
        StoreOwner storeOwner = getCurrentStoreOwner();
        if (storeOwner == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Không tìm thấy thông tin storeowner"));
        }

        ApiResponse<ServiceCategoryDTO> response = storeOwnerService.createServiceCategory(storeOwner, serviceCategoryDTO);

        if(response.getData() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        return  ResponseEntity.ok(response);
    }

    @PostMapping("/category/update")
    @PreAuthorize("hasRole('STOREOWNER')")
    public ResponseEntity<ApiResponse<ServiceCategoryDTO>> updateServiceCategory(@RequestBody ServiceCategoryDTO serviceCategoryDTO) {
        //Kiểm tra thông tin storeowner
        StoreOwner storeOwner = getCurrentStoreOwner();
        if (storeOwner == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Không tìm thấy thông tin storeowner"));
        }

        ApiResponse<ServiceCategoryDTO> response = storeOwnerService.updateServiceCategory(storeOwner, serviceCategoryDTO);

        if(response.getData() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        return  ResponseEntity.ok(response);
    }

    @PostMapping("/category/delete")
    @PreAuthorize("hasRole('STOREOWNER')")
    public ResponseEntity<ApiResponse<Integer>> deleteServiceCategory(@RequestBody ServiceCategoryDTO serviceCategoryDTO) {
        //Kiểm tra thông tin storeowner
        StoreOwner storeOwner = getCurrentStoreOwner();
        if (storeOwner == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Không tìm thấy thông tin storeowner"));
        }

        ApiResponse<Integer> response = storeOwnerService.deleteServiceCategory(storeOwner, serviceCategoryDTO);

        return  ResponseEntity.ok(response);
    }

    @PostMapping("/service/add")
    @PreAuthorize("hasRole('STOREOWNER')")
    public ResponseEntity<ApiResponse<ServiceDTO>> addService(@RequestBody ServiceDTO serviceDTO) {
        //Kiểm tra thông tin storeowner
        StoreOwner storeOwner = getCurrentStoreOwner();
        if (storeOwner == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Không tìm thấy thông tin storeowner"));
        }

        ApiResponse<ServiceDTO> response = storeOwnerService.createService(storeOwner, serviceDTO);

        if(response.getData() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        return  ResponseEntity.ok(response);
    }

    @PostMapping("/service/update")
    @PreAuthorize("hasRole('STOREOWNER')")
    public ResponseEntity<ApiResponse<ServiceDTO>> updateService(@RequestBody ServiceDTO serviceDTO) {
        //Kiểm tra thông tin storeowner
        StoreOwner storeOwner = getCurrentStoreOwner();
        if (storeOwner == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Không tìm thấy thông tin storeowner"));
        }

        ApiResponse<ServiceDTO> response = storeOwnerService.updateService(storeOwner, serviceDTO);

        if(response.getData() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        return  ResponseEntity.ok(response);
    }

    @PostMapping("/service/delete")
    @PreAuthorize("hasRole('STOREOWNER')")
    public ResponseEntity<ApiResponse<Integer>> deleteService(@RequestBody ServiceDTO serviceDTO) {
        //Kiểm tra thông tin storeowner
        StoreOwner storeOwner = getCurrentStoreOwner();
        if (storeOwner == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Không tìm thấy thông tin storeowner"));
        }

        ApiResponse<Integer> response = storeOwnerService.deleteService(storeOwner, serviceDTO);

        return  ResponseEntity.ok(response);
    }

    @GetMapping("/manager/service")
    @PreAuthorize("hasRole('STOREOWNER')")
    public ResponseEntity<PagedResponse<CategoryWithServiceDTO>> getAllCategoriesWithServicesByStore(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
      try {
        StoreOwner storeOwner = getCurrentStoreOwner();
        PagedResponse<CategoryWithServiceDTO> response = storeOwnerService.getAllCategoriesWithServicesByStore(storeOwner, page, size);
        return ResponseEntity.ok(response);
      } catch (Exception e) {
        return ResponseEntity.badRequest()
                .body(new PagedResponse<>("Lấy danh sách nhân viên thất bại: " + e.getMessage(), null));
      }
    }
}