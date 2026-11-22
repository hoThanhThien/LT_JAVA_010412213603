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
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

  @Autowired
  private com.example.laundry.security.LoginRateLimiter loginRateLimiter;

  @Autowired
  private com.example.laundry.repository.PaymentRepository paymentRepository;

  @Autowired
  private com.example.laundry.repository.OrderRepository orderRepository;

  @Autowired
  private com.example.laundry.repository.OrderStatusHistoryRepository orderStatusHistoryRepository;

  private MockMvc mockMvc;

  @BeforeEach
  void setUpMockMvc() {
    loginRateLimiter.resetAll();
    this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
        .addFilters(springSecurityFilterChain)
        .build();
  }

  @Test
  void forgotPasswordAndResetPasswordAllowLoginWithNewPassword() throws Exception {
    long suffix = System.currentTimeMillis() % 10000000L;
    String phone = "097" + String.format("%07d", suffix);
    String email = "forgot" + suffix + "@test.local";

    Customer customer = new Customer();
    customer.setUsername("Forgot User " + suffix);
    customer.setPhone(phone);
    customer.setEmail(email);
    customer.setPassword(passwordEncoder.encode("OldPass@123"));
    customer.setRoles(Roles.Customer);
    customerRepository.save(customer);

    String phoneOnlyPhone = "096" + String.format("%07d", suffix);
    Customer phoneOnlyCustomer = new Customer();
    phoneOnlyCustomer.setUsername("PhoneOnly " + suffix);
    phoneOnlyCustomer.setPhone(phoneOnlyPhone);
    phoneOnlyCustomer.setEmail(null);
    phoneOnlyCustomer.setPassword(passwordEncoder.encode("OldPass@123"));
    phoneOnlyCustomer.setRoles(Roles.Customer);
    customerRepository.save(phoneOnlyCustomer);

    Object targetAuthService = AopTestUtils.getTargetObject(authService);
    Object originalMailSender = ReflectionTestUtils.getField(targetAuthService, "mailSender");
    Object originalMailUsername = ReflectionTestUtils.getField(targetAuthService, "mailUsername");

    try {
      JavaMailSender mockMailSender = Mockito.mock(JavaMailSender.class);
      ReflectionTestUtils.setField(targetAuthService, "mailSender", mockMailSender);
      ReflectionTestUtils.setField(targetAuthService, "mailUsername", "noreply@heramo.test");

      mockMvc.perform(post("/auth/forgot-password")
              .contentType(MediaType.APPLICATION_JSON)
              .content("{\"identifier\":\"" + email + "\"}"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.data.emailSent").value(true))
          .andExpect(jsonPath("$.data.devOtp").doesNotExist());

      ArgumentCaptor<SimpleMailMessage> mailCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
      Mockito.verify(mockMailSender).send(mailCaptor.capture());
      String emailBody = mailCaptor.getValue().getText();
      assertNotNull(emailBody);
      java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("là:\\s*(\\d{6})\\b").matcher(emailBody);
      assertTrue(matcher.find(), "OTP 6 digits should be included in recovery email");
      String otp = matcher.group(1);

      mockMvc.perform(post("/auth/reset-password")
              .contentType(MediaType.APPLICATION_JSON)
              .content(String.format(
                  "{\"identifier\":\"%s\",\"otp\":\"000000\",\"newPassword\":\"NewPass@123\",\"confirmPassword\":\"NewPass@123\"}",
                  email
              )))
          .andExpect(status().isBadRequest());

      mockMvc.perform(post("/auth/reset-password")
              .contentType(MediaType.APPLICATION_JSON)
              .content(String.format(
                  "{\"identifier\":\"%s\",\"otp\":\"%s\",\"newPassword\":\"NewPass@123\",\"confirmPassword\":\"NewPass@123\"}",
                  email, otp
              )))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.message").value("Đặt lại mật khẩu thành công"));

      String newToken = loginAndExtractToken(phone, "NewPass@123", "accessToken");
      assertNotNull(newToken);
      assertFalse(newToken.isBlank());

      MvcResult fallbackResult = mockMvc.perform(post("/auth/forgot-password")
              .contentType(MediaType.APPLICATION_JSON)
              .content("{\"identifier\":\"" + phoneOnlyPhone + "\"}"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.data.emailSent").value(false))
          .andReturn();
      String devOtp = JsonPath.read(fallbackResult.getResponse().getContentAsString(), "$.data.devOtp");
      assertNotNull(devOtp);
      assertEquals(6, devOtp.length());

      mockMvc.perform(post("/auth/reset-password")
              .contentType(MediaType.APPLICATION_JSON)
              .content(String.format(
                  "{\"identifier\":\"%s\",\"otp\":\"%s\",\"newPassword\":\"ResetOk@456\",\"confirmPassword\":\"ResetOk@456\"}",
                  phoneOnlyPhone, devOtp
              )))
          .andExpect(status().isOk());

      String phoneOnlyToken = loginAndExtractToken(phoneOnlyPhone, "ResetOk@456", "accessToken");
      assertNotNull(phoneOnlyToken);
    } finally {
      ReflectionTestUtils.setField(targetAuthService, "mailSender", originalMailSender);
      ReflectionTestUtils.setField(targetAuthService, "mailUsername", originalMailUsername);
    }
  }

  @Test
  void authenticatedCustomerAndGoogleUserCanChangePassword() throws Exception {
    long suffix = System.currentTimeMillis() % 10000000L;
    String phone = "095" + String.format("%07d", suffix);

    Customer customer = new Customer();
    customer.setUsername("ChangePass User " + suffix);
    customer.setPhone(phone);
    customer.setEmail("changepass" + suffix + "@test.local");
    customer.setPassword(passwordEncoder.encode("InitPass@123"));
    customer.setRoles(Roles.Customer);
    customerRepository.save(customer);

    String accessToken = loginAndExtractToken(phone, "InitPass@123", "accessToken");

    mockMvc.perform(post("/accounts/change-password")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"currentPassword\":\"Wrong@123\",\"newPassword\":\"Changed@456\",\"confirmPassword\":\"Changed@456\"}"))
        .andExpect(status().isBadRequest());

    mockMvc.perform(post("/accounts/change-password")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"currentPassword\":\"\",\"newPassword\":\"Changed@456\",\"confirmPassword\":\"Changed@456\"}"))
        .andExpect(status().isBadRequest());

    mockMvc.perform(post("/accounts/change-password")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"currentPassword\":\"InitPass@123\",\"newPassword\":\"Changed@456\",\"confirmPassword\":\"Changed@456\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Đổi mật khẩu thành công"));

    String newToken = loginAndExtractToken(phone, "Changed@456", "accessToken");
    assertNotNull(newToken);

    Object targetAuthService = AopTestUtils.getTargetObject(authService);
    Object originalFirebaseAuth = ReflectionTestUtils.getField(targetAuthService, "firebaseAuth");
    String googleEmail = "googlecp" + suffix + "@gmail.com";

    try {
      FirebaseAuth mockFirebaseAuth = Mockito.mock(FirebaseAuth.class);
      FirebaseToken mockToken = Mockito.mock(FirebaseToken.class);
      Mockito.when(mockToken.getEmail()).thenReturn(googleEmail);
      Mockito.when(mockToken.isEmailVerified()).thenReturn(true);
      Mockito.when(mockToken.getName()).thenReturn("Google User");
      Mockito.when(mockToken.getPicture()).thenReturn("https://example.com/avt.png");
      Mockito.when(mockFirebaseAuth.verifyIdToken("mock-cp-google-token")).thenReturn(mockToken);
      ReflectionTestUtils.setField(targetAuthService, "firebaseAuth", mockFirebaseAuth);

      MvcResult googleLoginRes = mockMvc.perform(post("/auth/google")
              .contentType(MediaType.APPLICATION_JSON)
              .content("{\"idToken\":\"mock-cp-google-token\"}"))
          .andExpect(status().isOk())
          .andReturn();
      String googleAccessToken = JsonPath.read(googleLoginRes.getResponse().getContentAsString(), "$.data.accessToken");

      mockMvc.perform(post("/accounts/change-password")
              .header(HttpHeaders.AUTHORIZATION, "Bearer " + googleAccessToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content("{\"currentPassword\":\"\",\"newPassword\":\"GoogleSet@789\",\"confirmPassword\":\"GoogleSet@789\"}"))
          .andExpect(status().isOk());

      Customer reloadedGoogleCustomer = customerRepository.findByEmail(googleEmail).orElse(null);
      assertNotNull(reloadedGoogleCustomer);
      assertTrue(passwordEncoder.matches("GoogleSet@789", reloadedGoogleCustomer.getPassword()));
    } finally {
      ReflectionTestUtils.setField(targetAuthService, "firebaseAuth", originalFirebaseAuth);
    }
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

  @Test
  void serviceCategoryDtoFieldsAndCustomerBookServiceValidationWorkCorrectly() {
    StoreOwner owner = createOwnerWithShop("0970000001", "Order Test Shop");
    LaundryShop shop = laundryShopRepository.findByStoreOwner(owner);
    assertNotNull(shop);

    ServiceCategoryDTO catDto = new ServiceCategoryDTO();
    catDto.setName("Giặt Hấp VIP");
    catDto.setImageDesc("data:image/jpeg;base64,catimg");
    catDto.setDescription("Mô tả nhóm dịch vụ");
    ApiResponse<ServiceCategoryDTO> createdCat = storeOwnerService.createServiceCategory(owner, catDto);
    assertNotNull(createdCat.getData());
    assertEquals("data:image/jpeg;base64,catimg", createdCat.getData().getImageDesc());
    assertEquals("Mô tả nhóm dịch vụ", createdCat.getData().getDescription());

    ServiceDTO invalidPriceSvc = new ServiceDTO();
    invalidPriceSvc.setCategoryId(createdCat.getData().getId());
    invalidPriceSvc.setName("Invalid Price Svc");
    invalidPriceSvc.setDescription("Desc");
    invalidPriceSvc.setEstimatedTime("24h");
    invalidPriceSvc.setPrice(0.0);
    invalidPriceSvc.setImageDesc("img");
    assertNull(storeOwnerService.createService(owner, invalidPriceSvc).getData());

    ServiceDTO svcDto = new ServiceDTO();
    svcDto.setCategoryId(createdCat.getData().getId());
    svcDto.setName("Giặt Sấy Nhanh");
    svcDto.setDescription("Trong ngày");
    svcDto.setEstimatedTime("12h");
    svcDto.setPrice(30000.0);
    svcDto.setImageDesc("img");
    ApiResponse<ServiceDTO> createdSvc = storeOwnerService.createService(owner, svcDto);
    assertNotNull(createdSvc.getData());

    Customer customer = new Customer("Order Customer", passwordEncoder.encode("secret123"), "ordercust@test.local", "0970000002", "123 Profile St", Roles.Customer);
    Customer savedCustomer = customerRepository.save(customer);

    // Omitted address falls back to customer's profile address & BigDecimal totalAmount avoids float noise
    com.example.laundry.dto.OrderDTO orderDto = new com.example.laundry.dto.OrderDTO(
        null, null, null, "",
        shop.getId(), createdCat.getData().getId(), createdSvc.getData().getId(),
        1.1, null, "Gọi trước khi giao"
    );
    ApiResponse<com.example.laundry.dto.OrderResponse> orderRes = customerService.bookService(savedCustomer, orderDto);
    assertNotNull(orderRes.getData());
    assertEquals(33000.0, orderRes.getData().getTotalAmount());
    assertEquals("123 Profile St", orderRes.getData().getAddress());
  }

  @Test
  void googleCustomerCanBookOrderPersistInitialAddressViewHistoryAndGetPaymentQr() throws Exception {
    StoreOwner owner = createOwnerWithShop("0971000001", "Google Booking Shop");
    LaundryShop shop = laundryShopRepository.findByStoreOwner(owner);
    assertNotNull(shop);

    ServiceCategoryDTO catDto = new ServiceCategoryDTO();
    catDto.setName("Giặt Sấy E2E");
    catDto.setImageDesc("img");
    catDto.setDescription("Desc");
    Long categoryId = storeOwnerService.createServiceCategory(owner, catDto).getData().getId();

    ServiceDTO svcDto = new ServiceDTO();
    svcDto.setCategoryId(categoryId);
    svcDto.setName("Giặt Sấy Thường");
    svcDto.setDescription("Desc");
    svcDto.setEstimatedTime("24h");
    svcDto.setPrice(25000.0);
    svcDto.setImageDesc("img");
    Long serviceId = storeOwnerService.createService(owner, svcDto).getData().getId();

    Object targetAuth = AopTestUtils.getUltimateTargetObject(authService);
    FirebaseAuth mockFirebaseAuth = Mockito.mock(FirebaseAuth.class);
    FirebaseToken mockToken = Mockito.mock(FirebaseToken.class);
    Mockito.when(mockToken.getEmail()).thenReturn("googlebooking@gmail.com");
    Mockito.when(mockToken.isEmailVerified()).thenReturn(true);
    Mockito.when(mockToken.getName()).thenReturn("Google Booking User");
    Mockito.when(mockToken.getPicture()).thenReturn("https://example.com/avt.png");
    Mockito.when(mockFirebaseAuth.verifyIdToken("mock-google-booking-token")).thenReturn(mockToken);

    ReflectionTestUtils.setField(targetAuth, "firebaseAuth", mockFirebaseAuth);
    String accessToken;
    String customerId;
    try {
      MvcResult loginRes = mockMvc.perform(post("/auth/google")
              .contentType(MediaType.APPLICATION_JSON)
              .content("{\"idToken\":\"mock-google-booking-token\"}"))
          .andExpect(status().isOk())
          .andReturn();
      String loginJson = loginRes.getResponse().getContentAsString();
      accessToken = JsonPath.read(loginJson, "$.data.accessToken");
      customerId = JsonPath.read(loginJson, "$.data.account.id");
    } finally {
      ReflectionTestUtils.setField(targetAuth, "firebaseAuth", null);
    }

    String orderBody = String.format(
        "{\"laundryShop\":{\"id\":%d},\"serviceCategory\":{\"id\":%d},\"service\":{\"id\":%d},\"orderVolume\":2.0,\"phone\":\"0971234567\",\"address\":\"456 Le Loi, Q1\",\"instructions\":\"Nhan buoi sang\",\"imgProduct\":\"\"}",
        shop.getId(), categoryId, serviceId);

    MvcResult createRes = mockMvc.perform(post("/customer/orders/create")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(orderBody))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.totalAmount").value(50000.0))
        .andExpect(jsonPath("$.data.phone").value("0971234567"))
        .andExpect(jsonPath("$.data.address").value("456 Le Loi, Q1"))
        .andExpect(jsonPath("$.data.orderStatus").value("IN_PROGRESS"))
        .andReturn();

    Number createdOrderId = JsonPath.read(createRes.getResponse().getContentAsString(), "$.data.id");

    Customer reloaded = customerRepository.findById(java.util.UUID.fromString(customerId)).orElseThrow();
    assertEquals("456 Le Loi, Q1", reloaded.getAddress());
    assertEquals("0971234567", reloaded.getPhone());

    mockMvc.perform(get("/customer/orders/history")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.result[0].id").value(createdOrderId.longValue()))
        .andExpect(jsonPath("$.data.result[0].address").value("456 Le Loi, Q1"))
        .andExpect(jsonPath("$.data.result[0].paymentStatus").value("PENDING"));

    mockMvc.perform(get("/api/payments/info/" + createdOrderId.longValue())
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.orderId").value(createdOrderId.longValue()))
        .andExpect(jsonPath("$.totalAmount").value(50000.0))
        .andExpect(jsonPath("$.qrCode").isNotEmpty());
  }

  @Test
  void refreshTokenRotationPreventsReplayAndSupportsMultiDeviceSessions() throws Exception {
    String phone = "0981000001";
    Customer customer = new Customer(
        "MultiDevice User",
        passwordEncoder.encode("secret123"),
        "multidevice@test.local",
        phone,
        "HCMC",
        Roles.Customer
    );
    customerRepository.save(customer);

    MvcResult login1 = mockMvc.perform(post("/auth/login")
            .header(HttpHeaders.USER_AGENT, "Chrome-Desktop/1.0")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"phone\":\"" + phone + "\",\"password\":\"secret123\"}"))
        .andExpect(status().isOk())
        .andReturn();
    String refresh1 = JsonPath.read(login1.getResponse().getContentAsString(), "$.data.refreshToken");

    MvcResult login2 = mockMvc.perform(post("/auth/login")
            .header(HttpHeaders.USER_AGENT, "iPhone-App/2.0")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"phone\":\"" + phone + "\",\"password\":\"secret123\"}"))
        .andExpect(status().isOk())
        .andReturn();
    String access2 = JsonPath.read(login2.getResponse().getContentAsString(), "$.data.accessToken");
    String refresh2 = JsonPath.read(login2.getResponse().getContentAsString(), "$.data.refreshToken");

    MvcResult sessionsRes = mockMvc.perform(get("/auth/sessions")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + access2))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.length()").value(2))
        .andReturn();
    String sessionsJson = sessionsRes.getResponse().getContentAsString();
    Number sessionIdToRevoke = null;
    java.util.List<java.util.Map<String, Object>> sessionList = JsonPath.read(sessionsJson, "$.data");
    for (java.util.Map<String, Object> item : sessionList) {
      if ("iPhone-App/2.0".equals(item.get("deviceInfo"))) {
        sessionIdToRevoke = (Number) item.get("id");
        break;
      }
    }
    assertNotNull(sessionIdToRevoke);

    MvcResult rotateRes = mockMvc.perform(post("/auth/refresh-token")
            .header(HttpHeaders.USER_AGENT, "Chrome-Desktop/1.0")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"refreshToken\":\"" + refresh1 + "\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.accessToken").isNotEmpty())
        .andExpect(jsonPath("$.data.refreshToken").isNotEmpty())
        .andReturn();
    String rotatedRefresh1 = JsonPath.read(rotateRes.getResponse().getContentAsString(), "$.data.refreshToken");
    assertNotEquals(refresh1, rotatedRefresh1);

    // Replaying consumed refresh token must be rejected
    mockMvc.perform(post("/auth/refresh-token")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"refreshToken\":\"" + refresh1 + "\"}"))
        .andExpect(status().isForbidden());

    // Revoke device 2 session explicitly
    mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
            .delete("/auth/sessions/" + sessionIdToRevoke.longValue())
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + access2))
        .andExpect(status().isOk());

    mockMvc.perform(post("/auth/refresh-token")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"refreshToken\":\"" + refresh2 + "\"}"))
        .andExpect(status().isForbidden());

    mockMvc.perform(get("/auth/sessions")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + access2))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.length()").value(1));
  }

  @Test
  void loginRateLimiterBlocksAfterFiveFailedAttempts() throws Exception {
    String phone = "0982000001";
    Customer customer = new Customer(
        "RateLimit User",
        passwordEncoder.encode("Correct@123"),
        "ratelimit@test.local",
        phone,
        "HCMC",
        Roles.Customer
    );
    customerRepository.save(customer);

    for (int i = 0; i < 5; i++) {
      mockMvc.perform(post("/auth/login")
              .contentType(MediaType.APPLICATION_JSON)
              .content("{\"phone\":\"" + phone + "\",\"password\":\"wrong-pass\"}"))
          .andExpect(status().isUnauthorized());
    }

    // 6th attempt must trigger 429 Too Many Requests even with the right password
    mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"phone\":\"" + phone + "\",\"password\":\"Correct@123\"}"))
        .andExpect(status().isTooManyRequests());

    loginRateLimiter.resetAll();
    mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"phone\":\"" + phone + "\",\"password\":\"Correct@123\"}"))
        .andExpect(status().isOk());
  }

  @Test
  void paymentPollingPersistsPaidStatusAndPaymentRecordTransactionally() throws Exception {
    StoreOwner owner = createOwnerWithShop("0984000001", "Polling Tx Shop");
    LaundryShop shop = laundryShopRepository.findByStoreOwner(owner);
    assertNotNull(shop);

    ServiceCategoryDTO catDto = new ServiceCategoryDTO();
    catDto.setName("Giặt Sấy Tx");
    catDto.setImageDesc("img");
    catDto.setDescription("Desc");
    Long categoryId = storeOwnerService.createServiceCategory(owner, catDto).getData().getId();

    ServiceDTO svcDto = new ServiceDTO();
    svcDto.setCategoryId(categoryId);
    svcDto.setName("Giặt Nhanh Tx");
    svcDto.setDescription("Desc");
    svcDto.setEstimatedTime("12h");
    svcDto.setPrice(40000.0);
    svcDto.setImageDesc("img");
    Long serviceId = storeOwnerService.createService(owner, svcDto).getData().getId();

    String custPhone = "0984000002";
    Customer customer = new Customer(
        "Tx Customer",
        passwordEncoder.encode("TxPass@123"),
        "txcust@test.local",
        custPhone,
        "HCMC",
        Roles.Customer
    );
    customerRepository.save(customer);
    String token = loginAndExtractToken(custPhone, "TxPass@123", "accessToken");

    String orderBody = String.format(
        "{\"laundryShop\":{\"id\":%d},\"serviceCategory\":{\"id\":%d},\"service\":{\"id\":%d},\"orderVolume\":1.0,\"phone\":\"%s\",\"address\":\"10 Nguyen Hue\",\"instructions\":\"\",\"imgProduct\":\"\"}",
        shop.getId(), categoryId, serviceId, custPhone);

    MvcResult createRes = mockMvc.perform(post("/customer/orders/create")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(orderBody))
        .andExpect(status().isOk())
        .andReturn();
    Long orderId = ((Number) JsonPath.read(createRes.getResponse().getContentAsString(), "$.data.id")).longValue();

    CassoClient.CassoTransaction tx = new CassoClient.CassoTransaction();
    tx.setAmount(40000.0);
    tx.setDescription("Thanh toan LAUNDRY" + orderId + " test");
    tx.setTransactionTime(LocalDateTime.now().minusMinutes(3));

    CassoClient.CassoTransactionData cassoData = new CassoClient.CassoTransactionData();
    cassoData.setRecords(java.util.List.of(tx));

    CassoClient.CassoTransactionResponse cassoResponse = new CassoClient.CassoTransactionResponse();
    cassoResponse.setData(cassoData);

    CassoClient mockCasso = Mockito.mock(CassoClient.class);
    Mockito.when(mockCasso.getTransactions(Mockito.eq(1), Mockito.anyInt())).thenReturn(cassoResponse);

    Object targetPaymentService = AopTestUtils.getUltimateTargetObject(paymentService);
    Object originalCasso = ReflectionTestUtils.getField(targetPaymentService, "cassoClient");
    ReflectionTestUtils.setField(targetPaymentService, "cassoClient", mockCasso);

    try {
      paymentService.pollBankTransactions();

      var updatedOrder = orderRepository.findById(orderId).orElseThrow();
      assertEquals(com.example.laundry.models.order.PaymentStatus.PAID, updatedOrder.getPaymentStatus());

      var persistedPayment = paymentRepository.findByOrderId(orderId).orElse(null);
      assertNotNull(persistedPayment);
      assertEquals("PAID", persistedPayment.getStatus());
      assertEquals(40000.0, persistedPayment.getAmount());
      assertEquals("VIETQR_BANK_TRANSFER", persistedPayment.getPaymentMethod());
    } finally {
      ReflectionTestUtils.setField(targetPaymentService, "cassoClient", originalCasso);
    }
  }

  @Test
  void healthCheckReturnsUpAndUnknownRouteReturnsNotFound() throws Exception {
    mockMvc.perform(get("/health"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.status").value("UP"))
        .andExpect(jsonPath("$.data.database").value("UP"));

    mockMvc.perform(get("/actuator/health"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.status").value("UP"));

    String custPhone = "0985000001";
    Customer customer = new Customer(
        "Health Route Customer",
        passwordEncoder.encode("secret123"),
        "healthcust@test.local",
        custPhone,
        "HCMC",
        Roles.Customer
    );
    customerRepository.save(customer);
    String custToken = loginAndExtractToken(custPhone, "secret123", "accessToken");

    mockMvc.perform(get("/api/nonexistent-endpoint-check")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + custToken))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Không tìm thấy tài nguyên yêu cầu"));

    mockMvc.perform(get("/admin/all")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + custToken))
        .andExpect(status().isForbidden());
  }

  @Test
  void concurrentRefreshTokenRotationOnlyAllowsSingleWinner() throws Exception {
    String phone = "0983000001";
    Customer customer = new Customer(
        "Concurrent User",
        passwordEncoder.encode("secret123"),
        "concurrent@test.local",
        phone,
        "HCMC",
        Roles.Customer
    );
    customerRepository.save(customer);

    String refreshToken = loginAndExtractToken(phone, "secret123", "refreshToken");

    int threadCount = 5;
    java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newFixedThreadPool(threadCount);
    java.util.concurrent.CountDownLatch readyLatch = new java.util.concurrent.CountDownLatch(threadCount);
    java.util.concurrent.CountDownLatch startLatch = new java.util.concurrent.CountDownLatch(1);
    java.util.List<java.util.concurrent.Future<Integer>> futures = new java.util.ArrayList<>();

    try {
      for (int i = 0; i < threadCount; i++) {
        futures.add(executor.submit(() -> {
          readyLatch.countDown();
          startLatch.await(5, java.util.concurrent.TimeUnit.SECONDS);
          MvcResult res = mockMvc.perform(post("/auth/refresh-token")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content("{\"refreshToken\":\"" + refreshToken + "\"}"))
              .andReturn();
          return res.getResponse().getStatus();
        }));
      }

      assertTrue(readyLatch.await(5, java.util.concurrent.TimeUnit.SECONDS));
      startLatch.countDown();

      int okCount = 0;
      int forbiddenCount = 0;
      for (java.util.concurrent.Future<Integer> future : futures) {
        int status = future.get(15, java.util.concurrent.TimeUnit.SECONDS);
        if (status == 200) {
          okCount++;
        } else if (status == 403) {
          forbiddenCount++;
        }
      }

      assertEquals(1, okCount, "Exactly 1 concurrent refresh request may succeed");
      assertEquals(threadCount - 1, forbiddenCount, "All competing refresh requests must be rejected");
    } finally {
      executor.shutdownNow();
    }
  }

  @Test
  void registerRollsBackCustomerWhenRefreshTokenCreationFails() throws Exception {
    String phone = "0986000001";
    FirebaseToken fakeToken = Mockito.mock(FirebaseToken.class);
    Mockito.when(fakeToken.getClaims()).thenReturn(java.util.Map.of("phone_number", "+84" + phone.substring(1)));

    FirebaseAuth mockFirebaseAuth = Mockito.mock(FirebaseAuth.class);
    Mockito.when(mockFirebaseAuth.verifyIdToken("fake-register-token")).thenReturn(fakeToken);

    com.example.laundry.services.RefreshTokenService mockRefreshTokenService =
        Mockito.mock(com.example.laundry.services.RefreshTokenService.class);
    Mockito.when(mockRefreshTokenService.createRefreshToken(Mockito.any()))
        .thenThrow(new RuntimeException("Simulated refresh token failure"));

    Object targetCustomerService = AopTestUtils.getUltimateTargetObject(customerService);
    Object originalFirebaseAuth = ReflectionTestUtils.getField(targetCustomerService, "firebaseAuth");
    Object originalRefreshTokenService = ReflectionTestUtils.getField(targetCustomerService, "refreshTokenService");
    ReflectionTestUtils.setField(targetCustomerService, "firebaseAuth", mockFirebaseAuth);
    ReflectionTestUtils.setField(targetCustomerService, "refreshTokenService", mockRefreshTokenService);

    try {
      String payload = "{\"idToken\":\"fake-register-token\",\"username\":\"Rollback User\",\"password\":\"ValidPass@123\"}";
      mockMvc.perform(post("/auth/register")
              .contentType(MediaType.APPLICATION_JSON)
              .content(payload))
          .andExpect(status().isInternalServerError())
          .andExpect(header().exists("X-Request-Id"))
          .andExpect(jsonPath("$.message").value("Đã xảy ra lỗi hệ thống"));

      assertTrue(customerRepository.findByPhone(phone).isEmpty(),
          "Customer insert must roll back when downstream refresh token creation throws RuntimeException");
    } finally {
      ReflectionTestUtils.setField(targetCustomerService, "firebaseAuth", originalFirebaseAuth);
      ReflectionTestUtils.setField(targetCustomerService, "refreshTokenService", originalRefreshTokenService);
    }
  }

  @Test
  void paymentReconciliationRejectsAlphanumericSuffixCollisionAndDuplicateTransactionId() throws Exception {
    StoreOwner owner = createOwnerWithShop("0987000001", "Reconciliation Hardening Shop");
    LaundryShop shop = laundryShopRepository.findByStoreOwner(owner);
    assertNotNull(shop);

    ServiceCategoryDTO catDto = new ServiceCategoryDTO();
    catDto.setName("Giặt Sấy Hardened");
    catDto.setImageDesc("img");
    catDto.setDescription("Desc");
    Long categoryId = storeOwnerService.createServiceCategory(owner, catDto).getData().getId();

    ServiceDTO svcDto = new ServiceDTO();
    svcDto.setCategoryId(categoryId);
    svcDto.setName("Giặt Tiêu Chuẩn Hardened");
    svcDto.setDescription("Desc");
    svcDto.setEstimatedTime("24h");
    svcDto.setPrice(50000.0);
    svcDto.setImageDesc("img");
    Long serviceId = storeOwnerService.createService(owner, svcDto).getData().getId();

    String custPhone = "0987000002";
    Customer customer = new Customer(
        "Reconcile Customer",
        passwordEncoder.encode("ReconPass@123"),
        "reconcile@test.local",
        custPhone,
        "HCMC",
        Roles.Customer
    );
    customerRepository.save(customer);
    String token = loginAndExtractToken(custPhone, "ReconPass@123", "accessToken");

    String orderBody = String.format(
        "{\"laundryShop\":{\"id\":%d},\"serviceCategory\":{\"id\":%d},\"service\":{\"id\":%d},\"orderVolume\":1.0,\"phone\":\"%s\",\"address\":\"10 Le Loi\",\"instructions\":\"\",\"imgProduct\":\"\"}",
        shop.getId(), categoryId, serviceId, custPhone);

    MvcResult res1 = mockMvc.perform(post("/customer/orders/create")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(orderBody))
        .andExpect(status().isOk())
        .andReturn();
    Long order1Id = ((Number) JsonPath.read(res1.getResponse().getContentAsString(), "$.data.id")).longValue();

    MvcResult res2 = mockMvc.perform(post("/customer/orders/create")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(orderBody))
        .andExpect(status().isOk())
        .andReturn();
    Long order2Id = ((Number) JsonPath.read(res2.getResponse().getContentAsString(), "$.data.id")).longValue();

    CassoClient mockCasso = Mockito.mock(CassoClient.class);
    Object targetPaymentService = AopTestUtils.getUltimateTargetObject(paymentService);
    Object originalCasso = ReflectionTestUtils.getField(targetPaymentService, "cassoClient");
    ReflectionTestUtils.setField(targetPaymentService, "cassoClient", mockCasso);

    try {
      // Phase A: Alphanumeric suffix collision ("LAUNDRY<id>A") must NOT settle order1
      CassoClient.CassoTransaction suffixTx = new CassoClient.CassoTransaction();
      suffixTx.setTransactionId("TX-SUFFIX-001");
      suffixTx.setAmount(50000.0);
      suffixTx.setDescription("CK LAUNDRY" + order1Id + "A");
      suffixTx.setTransactionTime(LocalDateTime.now().minusMinutes(2));

      CassoClient.CassoTransactionData suffixData = new CassoClient.CassoTransactionData();
      suffixData.setRecords(java.util.List.of(suffixTx));
      CassoClient.CassoTransactionResponse suffixResp = new CassoClient.CassoTransactionResponse();
      suffixResp.setData(suffixData);
      Mockito.when(mockCasso.getTransactions(Mockito.eq(1), Mockito.anyInt())).thenReturn(suffixResp);

      paymentService.pollBankTransactions();
      assertEquals(
          com.example.laundry.models.order.PaymentStatus.PENDING,
          orderRepository.findById(order1Id).orElseThrow().getPaymentStatus(),
          "Alphanumeric suffix LAUNDRY<id>A must not match LAUNDRY<id>"
      );

      // Phase B: Duplicate external transactionId must settle only order1 and NOT order2
      CassoClient.CassoTransaction tx1 = new CassoClient.CassoTransaction();
      tx1.setTransactionId("TX-UNIQUE-001");
      tx1.setAmount(50000.0);
      tx1.setDescription("CK LAUNDRY" + order1Id + " thanh toan");
      tx1.setTransactionTime(LocalDateTime.now().minusMinutes(2));

      CassoClient.CassoTransaction tx2Duplicate = new CassoClient.CassoTransaction();
      tx2Duplicate.setTransactionId("TX-UNIQUE-001");
      tx2Duplicate.setAmount(50000.0);
      tx2Duplicate.setDescription("CK LAUNDRY" + order2Id + " thanh toan");
      tx2Duplicate.setTransactionTime(LocalDateTime.now().minusMinutes(1));

      CassoClient.CassoTransactionData dupData = new CassoClient.CassoTransactionData();
      dupData.setRecords(java.util.List.of(tx1, tx2Duplicate));
      CassoClient.CassoTransactionResponse dupResp = new CassoClient.CassoTransactionResponse();
      dupResp.setData(dupData);
      Mockito.when(mockCasso.getTransactions(Mockito.eq(1), Mockito.anyInt())).thenReturn(dupResp);

      paymentService.pollBankTransactions();

      assertEquals(
          com.example.laundry.models.order.PaymentStatus.PAID,
          orderRepository.findById(order1Id).orElseThrow().getPaymentStatus()
      );
      var payment1 = paymentRepository.findByOrderId(order1Id).orElseThrow();
      assertEquals("TX-UNIQUE-001", payment1.getExternalTransactionId());

      assertEquals(
          com.example.laundry.models.order.PaymentStatus.PENDING,
          orderRepository.findById(order2Id).orElseThrow().getPaymentStatus(),
          "Reused external transactionId must not settle a second order"
      );
      assertTrue(paymentRepository.findByOrderId(order2Id).isEmpty());
    } finally {
      ReflectionTestUtils.setField(targetPaymentService, "cassoClient", originalCasso);
    }
  }

  @Test
  void employeeOrderStatusEnforcesStateTransitionGuardAndRecordsAuditHistory() throws Exception {
    StoreOwner owner = createOwnerWithShop("0988000001", "Order Audit Shop");
    LaundryShop shop = laundryShopRepository.findByStoreOwner(owner);
    assertNotNull(shop);

    ServiceCategoryDTO catDto = new ServiceCategoryDTO();
    catDto.setName("Giặt Audit");
    catDto.setImageDesc("img");
    catDto.setDescription("Desc");
    Long categoryId = storeOwnerService.createServiceCategory(owner, catDto).getData().getId();

    ServiceDTO svcDto = new ServiceDTO();
    svcDto.setCategoryId(categoryId);
    svcDto.setName("Giặt Sấy Audit");
    svcDto.setDescription("Desc");
    svcDto.setEstimatedTime("24h");
    svcDto.setPrice(30000.0);
    svcDto.setImageDesc("img");
    Long serviceId = storeOwnerService.createService(owner, svcDto).getData().getId();

    String custPhone = "0988000002";
    Customer customer = new Customer(
        "Audit Customer",
        passwordEncoder.encode("AuditPass@123"),
        "auditcust@test.local",
        custPhone,
        "HCMC",
        Roles.Customer
    );
    customerRepository.save(customer);

    String empPhone = "0988000003";
    Employee employee = new Employee(
        "Audit Employee",
        passwordEncoder.encode("EmpPass@123"),
        "auditemp@test.local",
        empPhone,
        "HCMC",
        Roles.Employee,
        shop
    );
    employee.setStoreOwner(owner);
    employeeRepository.save(employee);

    String custToken = loginAndExtractToken(custPhone, "AuditPass@123", "accessToken");
    String empToken = loginAndExtractToken(empPhone, "EmpPass@123", "accessToken");

    String orderBody = String.format(
        "{\"laundryShop\":{\"id\":%d},\"serviceCategory\":{\"id\":%d},\"service\":{\"id\":%d},\"orderVolume\":1.0,\"phone\":\"%s\",\"address\":\"15 Hai Ba Trung\",\"instructions\":\"\",\"imgProduct\":\"\"}",
        shop.getId(), categoryId, serviceId, custPhone);

    MvcResult createRes = mockMvc.perform(post("/customer/orders/create")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + custToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(orderBody))
        .andExpect(status().isOk())
        .andReturn();
    Long orderId = ((Number) JsonPath.read(createRes.getResponse().getContentAsString(), "$.data.id")).longValue();

    // Valid forward transition: IN_PROGRESS -> DONE
    mockMvc.perform(put("/employee/" + orderId + "/status")
            .param("status", "DONE")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + empToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.orderStatus").value("DONE"));

    var history = orderStatusHistoryRepository.findByOrderIdOrderByChangedAtDesc(orderId);
    assertEquals(1, history.size());
    assertEquals(com.example.laundry.models.order.OrderStatus.IN_PROGRESS, history.get(0).getFromStatus());
    assertEquals(com.example.laundry.models.order.OrderStatus.DONE, history.get(0).getToStatus());
    assertEquals(employee.getId(), history.get(0).getChangedByUserId());

    // Invalid backward transition: DONE -> IN_PROGRESS must be rejected
    mockMvc.perform(put("/employee/" + orderId + "/status")
            .param("status", "IN_PROGRESS")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + empToken))
        .andExpect(status().isBadRequest());

    assertEquals(
        com.example.laundry.models.order.OrderStatus.DONE,
        orderRepository.findById(orderId).orElseThrow().getOrderStatus()
    );
    assertEquals(1, orderStatusHistoryRepository.findByOrderIdOrderByChangedAtDesc(orderId).size());
  }

  @Test
  void passwordChangeRevokesExistingRefreshTokensAndResponseIncludesRequestId() throws Exception {
    String phone = "0989000001";
    Customer customer = new Customer(
        "PwdRevoke Customer",
        passwordEncoder.encode("OldPass@123"),
        "pwdrevoke@test.local",
        phone,
        "HCMC",
        Roles.Customer
    );
    customerRepository.save(customer);

    MvcResult loginRes = mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"phone\":\"" + phone + "\",\"password\":\"OldPass@123\"}"))
        .andExpect(status().isOk())
        .andExpect(header().exists("X-Request-Id"))
        .andReturn();
    String loginBody = loginRes.getResponse().getContentAsString();
    String accessToken = JsonPath.read(loginBody, "$.data.accessToken");
    String refreshToken = JsonPath.read(loginBody, "$.data.refreshToken");

    MvcResult changeRes = mockMvc.perform(post("/accounts/change-password")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"currentPassword\":\"OldPass@123\",\"newPassword\":\"NewPass@456\",\"confirmPassword\":\"NewPass@456\"}"))
        .andExpect(status().isOk())
        .andExpect(header().exists("X-Request-Id"))
        .andReturn();
    String requestId = changeRes.getResponse().getHeader("X-Request-Id");
    assertNotNull(requestId);
    assertFalse(requestId.isBlank());

    // Client-supplied X-Request-Id should be echoed for distributed tracing
    mockMvc.perform(get("/health").header("X-Request-Id", "trace-req-12345"))
        .andExpect(status().isOk())
        .andExpect(header().string("X-Request-Id", "trace-req-12345"));

    // Existing refresh token must be revoked after password change
    mockMvc.perform(post("/auth/refresh-token")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"refreshToken\":\"" + refreshToken + "\"}"))
        .andExpect(status().isForbidden());
  }
}
