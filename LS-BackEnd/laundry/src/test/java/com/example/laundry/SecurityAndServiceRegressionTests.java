package com.example.laundry;

import com.example.laundry.client.CassoClient;
import com.example.laundry.dto.CustomerDTO;
import com.example.laundry.dto.EmployeeDTO;
import com.example.laundry.dto.LaundryShopDTO;
import com.example.laundry.dto.ServiceCategoryDTO;
import com.example.laundry.dto.ServiceDTO;
import com.example.laundry.dto.StoreOwnerDTO;
import com.example.laundry.models.shop.LaundryShop;
import com.example.laundry.models.user.Admin;
import com.example.laundry.models.user.Customer;
import com.example.laundry.models.user.Employee;
import com.example.laundry.models.user.Roles;
import com.example.laundry.models.user.StoreOwner;
import com.example.laundry.repository.AdminRepository;
import com.example.laundry.repository.CustomerRepository;
import com.example.laundry.repository.EmployeeRepository;
import com.example.laundry.repository.LaundryShopRepository;
import com.example.laundry.repository.ServiceCategoryRepository;
import com.example.laundry.repository.ServiceRepository;
import com.example.laundry.repository.StoreOwnerRepository;
import com.example.laundry.services.AdminService;
import com.example.laundry.services.AuthService;
import com.example.laundry.services.CustomerService;
import com.example.laundry.services.PaymentService;
import com.example.laundry.services.StoreOwnerService;
import com.example.laundry.utils.ApiResponse;
import com.example.laundry.utils.UserValidator;
import com.example.laundry.utils.VietQRCode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.jayway.jsonpath.JsonPath;
import jakarta.servlet.Filter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.AopTestUtils;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "app.cors.allowed-origins=http://localhost:3000, http://127.0.0.1:3000, https://oauth.casso.vn")
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

  @Autowired
  private AdminService adminService;

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private PaymentService paymentService;

  @Autowired
  private AuthService authService;

  @Autowired
  private VietQRCode vietQRCode;

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

  @Test
  void corsUsesCentralizedEnvConfigAndRejectsUntrustedOrigins() throws Exception {
    MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context)
        .addFilters(springSecurityFilterChain)
        .build();

    mockMvc.perform(options("/auth/login")
            .header(HttpHeaders.ORIGIN, "http://localhost:3000")
            .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST"))
        .andExpect(status().isOk())
        .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:3000"))
        .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true"));

    mockMvc.perform(options("/api/payments/info/1")
            .header(HttpHeaders.ORIGIN, "https://oauth.casso.vn")
            .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET"))
        .andExpect(status().isOk())
        .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "https://oauth.casso.vn"))
        .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true"));

    mockMvc.perform(options("/auth/login")
            .header(HttpHeaders.ORIGIN, "https://evil.example")
            .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST"))
        .andExpect(status().isForbidden())
        .andExpect(header().doesNotExist(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN));
  }

  @Test
  void refreshTokenCannotBeUsedAsAccessTokenAndStillRefreshesSession() throws Exception {
    Customer customer = new Customer("Refresh Guard", passwordEncoder.encode("secret123"), "refreshguard@test.local", "0960000001", "HCMC", Roles.Customer);
    customerRepository.save(customer);

    MvcResult loginRes = mockMvc.perform(post("/auth/login")
            .header("Authorization", "Bearer expired-or-garbage-token")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"phone\":\"0960000001\",\"password\":\"secret123\"}"))
        .andExpect(status().isOk())
        .andReturn();
    String json = loginRes.getResponse().getContentAsString();
    String accessToken = JsonPath.read(json, "$.data.accessToken");
    String refreshToken = JsonPath.read(json, "$.data.refreshToken");

    // Refresh token must NOT authenticate protected endpoints
    mockMvc.perform(get("/accounts/me")
            .header("Authorization", "Bearer " + refreshToken))
        .andExpect(status().isUnauthorized());

    // Passing accessToken to /auth/refresh-token must be rejected
    mockMvc.perform(post("/auth/refresh-token")
            .contentType(MediaType.APPLICATION_JSON)
            .content(String.format("{\"refreshToken\":\"%s\"}", accessToken)))
        .andExpect(status().isForbidden());

    // Frontend sends Authorization: Bearer <refreshToken> to /auth/refresh-token — must keep working
    mockMvc.perform(post("/auth/refresh-token")
            .header("Authorization", "Bearer " + refreshToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(String.format("{\"refreshToken\":\"%s\"}", refreshToken)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.accessToken").isNotEmpty());
  }

  @Test
  void logoutBlacklistsAccessTokenEvenWhenRefreshTokenMissingOrInvalid() throws Exception {
    Customer customer = new Customer("Logout NoRefresh", passwordEncoder.encode("secret123"), "logoutnr@test.local", "0960000002", "HCMC", Roles.Customer);
    customerRepository.save(customer);

    String accessToken = loginAndExtractToken("0960000002", "secret123", "accessToken");

    mockMvc.perform(post("/auth/logout")
            .contentType(MediaType.APPLICATION_JSON)
            .content(String.format("{\"accessToken\":\"%s\",\"refreshToken\":\"invalid-or-missing-refresh\"}", accessToken)))
        .andExpect(status().isOk());

    mockMvc.perform(get("/accounts/me")
            .header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void crossRoleDuplicatePhoneAndEmailAreRejected() {
    Customer customer = new Customer("Cross Role", passwordEncoder.encode("secret123"), "cross@test.local", "0960000003", "HCMC", Roles.Customer);
    customerRepository.save(customer);

    StoreOwnerDTO dupPhoneOwner = new StoreOwnerDTO();
    dupPhoneOwner.setUsername("Owner DupPhone");
    dupPhoneOwner.setPassword("Secret@123");
    dupPhoneOwner.setEmail("owner-unique@test.local");
    dupPhoneOwner.setPhone("0960000003");
    dupPhoneOwner.setAddress("HCMC");
    ApiResponse<StoreOwner> ownerRes = adminService.createStoreOwner(dupPhoneOwner);
    assertNull(ownerRes.getData());

    StoreOwner owner = createOwnerWithShop("0960000013", "Cross Shop");
    EmployeeDTO dupEmailEmp = new EmployeeDTO();
    dupEmailEmp.setUsername("Emp DupEmail");
    dupEmailEmp.setPassword("Secret!123");
    dupEmailEmp.setEmail("cross@test.local");
    dupEmailEmp.setPhone("0960000023");
    dupEmailEmp.setAddress("HCMC");
    ApiResponse<EmployeeDTO> empRes = storeOwnerService.createEmployee(owner, dupEmailEmp);
    assertNull(empRes.getData());

    assertTrue(UserValidator.isValidPassword("Secret!123"));
    assertTrue(UserValidator.isValidPassword("Secret_123"));
  }

  @Test
  void vietQrAndCassoTransactionMatchingHandleFormattingAndBoundariesSafely() {
    String wholeAmountUrl = vietQRCode.generateQRCode("MB", "123456", "35000.0", "LAUNDRY9", "TEST");
    assertTrue(wholeAmountUrl.contains("amount=35000&"));

    String fractionalAmountUrl = vietQRCode.generateQRCode("MB", "123456", "1000.05", "LAUNDRY9", "TEST");
    assertTrue(fractionalAmountUrl.contains("amount=1000.05&"));

    CassoClient.CassoTransaction txTen = new CassoClient.CassoTransaction();
    txTen.setDescription("CHUYEN KHOAN LAUNDRY10");
    txTen.setAmount(100000.0);
    txTen.setTransactionTime(LocalDateTime.now().minusMinutes(5));
    Boolean matchedTen = ReflectionTestUtils.invokeMethod(paymentService, "isMatchingTransaction", txTen, "LAUNDRY1", 50000.0);
    assertEquals(Boolean.FALSE, matchedTen);

    CassoClient.CassoTransaction txOne = new CassoClient.CassoTransaction();
    txOne.setDescription("MBVCB.LAUNDRY1.CT");
    txOne.setAmount(50000.0);
    txOne.setTransactionTime(LocalDateTime.now().minusMinutes(5));
    Boolean matchedOne = ReflectionTestUtils.invokeMethod(paymentService, "isMatchingTransaction", txOne, "LAUNDRY1", 50000.0);
    assertEquals(Boolean.TRUE, matchedOne);

    txOne.setAmount(null);
    Boolean nullAmountMatch = ReflectionTestUtils.invokeMethod(paymentService, "isMatchingTransaction", txOne, "LAUNDRY1", 50000.0);
    assertEquals(Boolean.FALSE, nullAmountMatch);
  }

  @Test
  void createShopRelinksExistingEmployeesWithoutShop() {
    StoreOwner owner = new StoreOwner("Rebuild Owner", passwordEncoder.encode("secret123"), "rebuild@test.local", "0960000004", "HCMC", Roles.StoreOwner);
    StoreOwner savedOwner = storeOwnerRepository.save(owner);

    LaundryShopDTO shop1Dto = new LaundryShopDTO();
    shop1Dto.setName("Initial Shop");
    shop1Dto.setAddress("123 Street");
    ApiResponse<LaundryShopDTO> shop1Res = storeOwnerService.createShop(savedOwner, shop1Dto);
    assertNotNull(shop1Res.getData());

    Employee emp = new Employee("Relink Emp", passwordEncoder.encode("secret123"), "relink-emp@test.local", "0960000005", "HCMC", Roles.Employee, savedOwner);
    emp.setShop(null);
    Employee savedEmp = employeeRepository.save(emp);

    // Simulate owner recreating shop cleanly
    LaundryShop shop1 = laundryShopRepository.findByStoreOwner(savedOwner);
    assertNotNull(shop1);
    laundryShopRepository.delete(shop1);

    LaundryShopDTO shop2Dto = new LaundryShopDTO();
    shop2Dto.setName("Rebuilt Shop");
    shop2Dto.setAddress("456 Street");
    ApiResponse<LaundryShopDTO> shop2Res = storeOwnerService.createShop(savedOwner, shop2Dto);
    assertNotNull(shop2Res.getData());

    Employee reloadedEmp = employeeRepository.findById(savedEmp.getId()).orElseThrow();
    assertNotNull(reloadedEmp.getShop());
    assertEquals(shop2Res.getData().getId(), reloadedEmp.getShop().getId());
  }

  @Test
  void invalidPaginationReturnsBadRequest() throws Exception {
    Admin admin = new Admin("Page Admin", passwordEncoder.encode("secret123"), "pageadmin@test.local", "0960000006", "HCMC", Roles.Admin);
    adminRepository.save(admin);
    String adminToken = loginAndExtractToken("0960000006", "secret123", "accessToken");

    mockMvc.perform(get("/admin/customers")
            .header("Authorization", "Bearer " + adminToken)
            .param("page", "-1")
            .param("size", "10"))
        .andExpect(status().isBadRequest());

    mockMvc.perform(get("/admin/customers")
            .header("Authorization", "Bearer " + adminToken)
            .param("page", "0")
            .param("size", "101"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void openApiAndSwaggerUiEndpointsAreAccessibleWithBearerScheme() throws Exception {
    mockMvc.perform(get("/v3/api-docs"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.openapi").exists())
        .andExpect(jsonPath("$.components.securitySchemes.bearerAuth.scheme").value("bearer"))
        .andExpect(jsonPath("$.paths['/auth/login']").exists());

    mockMvc.perform(get("/v3/api-docs")
            .header(HttpHeaders.AUTHORIZATION, "Bearer invalid-stale-token"))
        .andExpect(status().isOk());

    mockMvc.perform(get("/swagger-ui.html"))
        .andExpect(status().isFound());

    mockMvc.perform(get("/swagger-ui/index.html"))
        .andExpect(status().isOk());
  }

  @Test
  void googleLoginRejectsMissingAndInvalidTokens() throws Exception {
    mockMvc.perform(post("/auth/google")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{}"))
        .andExpect(status().isUnauthorized());

    mockMvc.perform(post("/auth/google")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"idToken\":\"invalid-google-token\"}"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void googleLoginCreatesNewCustomerOnceReusesOnReloginAndBlocksEmptyPhoneLogin() throws Exception {
    Object targetAuth = AopTestUtils.getUltimateTargetObject(authService);
    FirebaseAuth mockFirebaseAuth = Mockito.mock(FirebaseAuth.class);
    FirebaseToken mockToken = Mockito.mock(FirebaseToken.class);
    Mockito.when(mockToken.getEmail()).thenReturn("newgoogleuser@gmail.com");
    Mockito.when(mockToken.isEmailVerified()).thenReturn(true);
    Mockito.when(mockToken.getName()).thenReturn("Google Test User");
    Mockito.when(mockToken.getPicture()).thenReturn("https://example.com/avatar.png");
    Mockito.when(mockFirebaseAuth.verifyIdToken("mock-firebase-id-token")).thenReturn(mockToken);

    ReflectionTestUtils.setField(targetAuth, "firebaseAuth", mockFirebaseAuth);
    try {
      MvcResult firstResult = mockMvc.perform(post("/auth/google")
              .contentType(MediaType.APPLICATION_JSON)
              .content("{\"idToken\":\"mock-firebase-id-token\"}"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.data.accessToken").isNotEmpty())
          .andExpect(jsonPath("$.data.refreshToken").isNotEmpty())
          .andExpect(jsonPath("$.data.account.email").value("newgoogleuser@gmail.com"))
          .andExpect(jsonPath("$.data.account.role").value("Customer"))
          .andReturn();

      String firstJson = firstResult.getResponse().getContentAsString();
      assertNull(JsonPath.read(firstJson, "$.data.account.phone"));
      String firstId = JsonPath.read(firstJson, "$.data.account.id");
      String accessToken = JsonPath.read(firstJson, "$.data.accessToken");

      Customer saved = customerRepository.findByEmail("newgoogleuser@gmail.com").orElseThrow();
      assertNull(saved.getPhone());
      assertEquals(firstId, saved.getId().toString());

      mockMvc.perform(get("/accounts/me")
              .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.data.account.email").value("newgoogleuser@gmail.com"));

      MvcResult secondResult = mockMvc.perform(post("/auth/google")
              .contentType(MediaType.APPLICATION_JSON)
              .content("{\"idToken\":\"mock-firebase-id-token\"}"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.data.account.id").value(firstId))
          .andReturn();
      assertEquals(firstId, JsonPath.read(secondResult.getResponse().getContentAsString(), "$.data.account.id"));

      mockMvc.perform(post("/auth/login")
              .contentType(MediaType.APPLICATION_JSON)
              .content("{\"phone\":null,\"password\":\"secret\"}"))
          .andExpect(status().isUnauthorized());

      mockMvc.perform(post("/auth/login")
              .contentType(MediaType.APPLICATION_JSON)
              .content("{\"phone\":\"\",\"password\":\"secret\"}"))
          .andExpect(status().isUnauthorized());
    } finally {
      ReflectionTestUtils.setField(targetAuth, "firebaseAuth", null);
    }
  }
}
