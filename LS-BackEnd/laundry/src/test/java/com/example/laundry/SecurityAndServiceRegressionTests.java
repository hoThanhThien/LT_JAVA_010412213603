package com.example.laundry;

import com.example.laundry.dto.CustomerDTO;
import com.example.laundry.dto.ServiceCategoryDTO;
import com.example.laundry.dto.ServiceDTO;
import com.example.laundry.models.shop.LaundryShop;
import com.example.laundry.models.user.Admin;
import com.example.laundry.models.user.Customer;
import com.example.laundry.models.user.Roles;
import com.example.laundry.models.user.StoreOwner;
import com.example.laundry.repository.AdminRepository;
import com.example.laundry.repository.CustomerRepository;
import com.example.laundry.repository.LaundryShopRepository;
import com.example.laundry.repository.ServiceCategoryRepository;
import com.example.laundry.repository.ServiceRepository;
import com.example.laundry.repository.StoreOwnerRepository;
import com.example.laundry.services.CustomerService;
import com.example.laundry.services.StoreOwnerService;
import com.example.laundry.utils.ApiResponse;
import com.jayway.jsonpath.JsonPath;
import jakarta.servlet.Filter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class SecurityAndServiceRegressionTests {

  @Autowired
  private WebApplicationContext context;

  @Autowired
  @Qualifier("springSecurityFilterChain")
  private Filter springSecurityFilterChain;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private AdminRepository adminRepository;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private StoreOwnerRepository storeOwnerRepository;

  @Autowired
  private LaundryShopRepository laundryShopRepository;

  @Autowired
  private ServiceCategoryRepository serviceCategoryRepository;

  @Autowired
  private ServiceRepository serviceRepository;

  @Autowired
  private StoreOwnerService storeOwnerService;

  @Autowired
  private CustomerService customerService;

  private MockMvc mockMvc;

  @BeforeEach
  void setUpMockMvc() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
        .addFilters(springSecurityFilterChain)
        .build();
  }

  private String loginAndExtractToken(String phone, String rawPassword, String field) throws Exception {
    String body = String.format("{\"phone\":\"%s\",\"password\":\"%s\"}", phone, rawPassword);
    MvcResult result = mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isOk())
        .andReturn();
    return JsonPath.read(result.getResponse().getContentAsString(), "$.data." + field);
  }

  private StoreOwner createOwnerWithShop(String phone, String shopName) {
    StoreOwner owner = new StoreOwner();
    owner.setUsername("Owner " + phone);
    owner.setPassword(passwordEncoder.encode("secret123"));
    owner.setEmail(phone + "@test.local");
    owner.setPhone(phone);
    owner.setAddress("HCMC");
    owner.setRoles(Roles.StoreOwner);
    StoreOwner savedOwner = storeOwnerRepository.save(owner);

    LaundryShop shop = new LaundryShop();
    shop.setName(shopName);
    shop.setAddress("HCMC");
    shop.setOpeningHours("08:00-21:00");
    shop.setDescription("Test shop");
    shop.setAverageRating(5.0);
    shop.setCreatedAt(new Date());
    shop.setStoreOwner(savedOwner);
    laundryShopRepository.save(shop);
    return savedOwner;
  }

  @Test
  void rbacBlocksUnauthorizedAndCrossRoleAccessAndAllowsPublicEndpoints() throws Exception {
    Admin admin = new Admin("Admin RBAC", passwordEncoder.encode("secret123"), "admin-rbac@test.local", "0910000001", "HCMC", Roles.Admin);
    adminRepository.save(admin);

    Customer customer = new Customer("Customer RBAC", passwordEncoder.encode("secret123"), "cust-rbac@test.local", "0910000002", "HCMC", Roles.Customer);
    customerRepository.save(customer);

    StoreOwner owner = createOwnerWithShop("0910000003", "Shop RBAC");

    // Unauthenticated protected routes must return 401
    mockMvc.perform(get("/admin/customers")).andExpect(status().isUnauthorized());
    mockMvc.perform(get("/storeowner/shop")).andExpect(status().isUnauthorized());
    mockMvc.perform(get("/accounts/me")).andExpect(status().isUnauthorized());

    // Public endpoints
    mockMvc.perform(get("/shops")).andExpect(status().isOk());

    // Public registration endpoints must not require prior authentication (not 401/403)
    int authRegStatus = mockMvc.perform(post("/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{}"))
        .andReturn().getResponse().getStatus();
    assertNotEquals(401, authRegStatus);
    assertNotEquals(403, authRegStatus);

    int custRegStatus = mockMvc.perform(post("/customer/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{}"))
        .andReturn().getResponse().getStatus();
    assertNotEquals(401, custRegStatus);
    assertNotEquals(403, custRegStatus);

    // Invalid login returns 401
    mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"phone\":\"0910000002\",\"password\":\"wrongpass\"}"))
        .andExpect(status().isUnauthorized());

    String customerToken = loginAndExtractToken("0910000002", "secret123", "accessToken");
    String adminToken = loginAndExtractToken("0910000001", "secret123", "accessToken");
    String ownerToken = loginAndExtractToken(owner.getPhone(), "secret123", "accessToken");

    // Customer cannot access Admin or StoreOwner routes
    mockMvc.perform(get("/admin/customers")
            .header("Authorization", "Bearer " + customerToken))
        .andExpect(status().isForbidden());

    mockMvc.perform(get("/storeowner/shop")
            .header("Authorization", "Bearer " + customerToken))
        .andExpect(status().isForbidden());

    // Admin and StoreOwner can access their own routes
    mockMvc.perform(get("/admin/customers")
            .header("Authorization", "Bearer " + adminToken))
        .andExpect(status().isOk());

    mockMvc.perform(get("/storeowner/shop")
            .header("Authorization", "Bearer " + ownerToken))
        .andExpect(status().isOk());
  }

  @Test
  void duplicateUsernamesResolveDistinctUsersByUuidJwtSubject() throws Exception {
    Customer c1 = new Customer("Duplicate Name", passwordEncoder.encode("secret123"), "dup1@test.local", "0920000001", "HCMC", Roles.Customer);
    customerRepository.save(c1);

    Customer c2 = new Customer("Duplicate Name", passwordEncoder.encode("secret123"), "dup2@test.local", "0920000002", "HCMC", Roles.Customer);
    Customer savedC2 = customerRepository.save(c2);

    String c2Token = loginAndExtractToken("0920000002", "secret123", "accessToken");

    mockMvc.perform(get("/accounts/me")
            .header("Authorization", "Bearer " + c2Token))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.account.phone").value("0920000002"))
        .andExpect(jsonPath("$.data.account.id").value(savedC2.getId().toString()));
  }

  @Test
  void logoutBlacklistsAccessTokenAndBlocksReuse() throws Exception {
    Customer customer = new Customer("Logout User", passwordEncoder.encode("secret123"), "logout@test.local", "0930000001", "HCMC", Roles.Customer);
    customerRepository.save(customer);

    String loginBody = "{\"phone\":\"0930000001\",\"password\":\"secret123\"}";
    MvcResult loginRes = mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(loginBody))
        .andExpect(status().isOk())
        .andReturn();
    String json = loginRes.getResponse().getContentAsString();
    String accessToken = JsonPath.read(json, "$.data.accessToken");
    String refreshToken = JsonPath.read(json, "$.data.refreshToken");

    mockMvc.perform(get("/accounts/me")
            .header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isOk());

    String logoutBody = String.format("{\"accessToken\":\"%s\",\"refreshToken\":\"%s\"}", accessToken, refreshToken);
    mockMvc.perform(post("/auth/logout")
            .header("Authorization", "Bearer " + accessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(logoutBody))
        .andExpect(status().isOk());

    mockMvc.perform(get("/accounts/me")
            .header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void serviceCategoryCompositeUniquenessAndStoreOwnerIdorProtection() {
    StoreOwner ownerA = createOwnerWithShop("0940000001", "Shop A");
    StoreOwner ownerB = createOwnerWithShop("0940000002", "Shop B");

    ServiceCategoryDTO catDto = new ServiceCategoryDTO();
    catDto.setName("Giặt Hấp Test");
    catDto.setDescription("Desc");
    catDto.setImageDesc("img");

    ApiResponse<ServiceCategoryDTO> createdA = storeOwnerService.createServiceCategory(ownerA, catDto);
    assertNotNull(createdA.getData());
    Long catAId = createdA.getData().getId();
    assertNotNull(catAId);

    // Same shop duplicate category name must fail
    ApiResponse<ServiceCategoryDTO> dupInSameShop = storeOwnerService.createServiceCategory(ownerA, catDto);
    assertNull(dupInSameShop.getData());

    // Different shop with same category name must succeed
    ApiResponse<ServiceCategoryDTO> createdB = storeOwnerService.createServiceCategory(ownerB, catDto);
    assertNotNull(createdB.getData());

    // Create service under catA
    ServiceDTO serviceDto = new ServiceDTO();
    serviceDto.setCategoryId(catAId);
    serviceDto.setName("Áo Sơ Mi");
    serviceDto.setDescription("Giặt ủi phẳng");
    serviceDto.setEstimatedTime("24h");
    serviceDto.setPrice(30000.0);
    serviceDto.setImageDesc("img");

    ApiResponse<ServiceDTO> createdService = storeOwnerService.createService(ownerA, serviceDto);
    assertNotNull(createdService.getData());
    Long serviceAId = createdService.getData().getId();
    assertNotNull(serviceAId);

    // Owner B attempting to update/delete Owner A's category must fail (IDOR protection)
    ServiceCategoryDTO hijackCat = new ServiceCategoryDTO();
    hijackCat.setId(catAId);
    hijackCat.setName("Hacked");
    ApiResponse<ServiceCategoryDTO> hijackCatUpdate = storeOwnerService.updateServiceCategory(ownerB, hijackCat);
    assertNull(hijackCatUpdate.getData());

    ServiceCategoryDTO delCat = new ServiceCategoryDTO();
    delCat.setId(catAId);
    ApiResponse<Integer> hijackCatDelete = storeOwnerService.deleteServiceCategory(ownerB, delCat);
    assertTrue(hijackCatDelete.getData() == null || hijackCatDelete.getData() == 0);
    assertTrue(serviceCategoryRepository.findById(catAId).isPresent());

    // Owner B attempting to update/delete Owner A's service must fail (IDOR protection)
    ServiceDTO hijackSvc = new ServiceDTO();
    hijackSvc.setId(serviceAId);
    hijackSvc.setName("Hacked Svc");
    hijackSvc.setPrice(1.0);
    ApiResponse<ServiceDTO> hijackSvcUpdate = storeOwnerService.updateService(ownerB, hijackSvc);
    assertNull(hijackSvcUpdate.getData());

    ServiceDTO delSvc = new ServiceDTO();
    delSvc.setId(serviceAId);
    ApiResponse<Integer> hijackSvcDelete = storeOwnerService.deleteService(ownerB, delSvc);
    assertTrue(hijackSvcDelete.getData() == null || hijackSvcDelete.getData() == 0);
    assertTrue(serviceRepository.findById(serviceAId).isPresent());
  }

  @Test
  void updateCustomerPersistsAvatarAndNeverExposesPassword() {
    String encodedPass = passwordEncoder.encode("secret123");
    Customer customer = new Customer("Avatar User", encodedPass, "avt@test.local", "0950000001", "Old Address", Roles.Customer);
    Customer saved = customerRepository.save(customer);

    CustomerDTO updateDto = new CustomerDTO();
    updateDto.setUsername("Avatar Updated");
    updateDto.setEmail("avt2@test.local");
    updateDto.setAddress("New Address");
    updateDto.setAvtUser("data:image/png;base64,iVBORw0KGgo=");

    ApiResponse<CustomerDTO> res = customerService.updateCustomer(saved, updateDto);
    assertNotNull(res.getData());
    assertEquals("data:image/png;base64,iVBORw0KGgo=", res.getData().getAvtUser());
    assertNull(res.getData().getPassword());

    Customer reloaded = customerRepository.findById(saved.getId()).orElseThrow();
    assertEquals("data:image/png;base64,iVBORw0KGgo=", reloaded.getAvtUser());
    assertEquals("New Address", reloaded.getAddress());
  }
}
