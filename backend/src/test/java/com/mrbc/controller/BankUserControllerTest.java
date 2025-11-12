package com.mrbc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrbc.controller.user.BankUserController;
import com.mrbc.dto.AuthRequest;
import com.mrbc.dto.AuthResponse;
import com.mrbc.dto.UserRegistrationRequest;
import com.mrbc.model.BankUser;
import com.mrbc.repository.user.BankUserRepository;
import com.mrbc.service.auth.AuthService;
import com.mrbc.service.user.BankUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BankUserController.class)
@Import(org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration.class)
@ActiveProfiles("dev")
class BankUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BankUserService bankUserService;

    @Autowired
    private BankUserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthService authService;

    private BankUser user;

    private String jwtToken;  // Store the JWT token here

    @BeforeEach
    void setUp() throws Exception {
        // Register the user
        UserRegistrationRequest registrationRequest = new UserRegistrationRequest();
        registrationRequest.setName("Shiva Shankar");
        registrationRequest.setEmail("shiva@example.com");
        registrationRequest.setPhoneNumber("1234567890");
        registrationRequest.setAddress("Example address, Scarborough");
        registrationRequest.setPassword("password123");

        // Call register method to save user in database
        user = authService.register(registrationRequest);

        // Login to obtain JWT token
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail("shiva@example.com");
        authRequest.setPassword("password123");

        AuthResponse authResponse = authService.login(authRequest);
        jwtToken = authResponse.getToken();

        // Mock SecurityContext
        SecurityContext context = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(context);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken("shiva@example.com", "password123", List.of(new SimpleGrantedAuthority("ROLE_USER")));
        Mockito.when(context.getAuthentication()).thenReturn(authenticationToken);
    }

    @Test
    void testCreateUser() throws Exception {
        // Create the user to be tested
        BankUser newUser = BankUser.builder()
                .name("John Doe")
                .email("john@example.com")
                .phoneNumber("9876543210")
                .address("Test address")
                .build();

        // Mock the service call to return the created user
        when(bankUserService.createUser(Mockito.any(BankUser.class))).thenReturn(newUser);

        // Perform the request using the JWT token for authentication
        mockMvc
                .perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser))
                        .header("Authorization", "Bearer " + jwtToken))  // Add JWT token to request header
                .andExpect(status().isOk())  // Expect HTTP 200 OK
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }


//
//@WebMvcTest(BankUserController.class)
//@Import(org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration.class)
//@ActiveProfiles("dev")
//class BankUserControllerTest {
//
//    private final MockMvc mockMvc;
//
//    @MockitoBean
//    private BankUserService bankUserService;
//
//    private final ObjectMapper objectMapper;
//
//    private BankUser user;
//
//    @Autowired
//    public BankUserControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {
//        this.mockMvc = mockMvc;
//        this.objectMapper = objectMapper;
//    }

//    @BeforeEach
//    void setUp() {
//        user = BankUser.builder()
//                .id(1L)
//                .name("Shiva Shankar")
//                .email("shiva@example.com")
//                .phoneNumber("1234567890")
//                .address("Example address, Scarborough")
//                .build();
//    }

//    @Test
//    @WithMockUser(username = "testuser", password = "password", roles = {"ROLE_USER"})
//        // Mock a user with ROLE_USER
//    void testCreateUser() throws Exception {
//        when(bankUserService.createUser(Mockito.any(BankUser.class))).thenReturn(user);
//
//        mockMvc.perform(
//                        post("/api/v1/users")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(objectMapper.writeValueAsString(user)))
//                .andExpect(status().isOk())  // Ensure the status is 200
//                .andExpect(jsonPath("$.name").value("Shiva Shankar"))
//                .andExpect(jsonPath("$.email").value("shiva@example.com"));
//    }

    @Test
    void testGetAllUsers() throws Exception {
        when(bankUserService.getAllUsers()).thenReturn(List.of(user));

        mockMvc
                .perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].email").value("shiva@example.com"));
    }

    @Test
    void testGetUserById() throws Exception {
        when(bankUserService.getUserById(1L)).thenReturn(user);

        mockMvc
                .perform(get("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Shiva Shankar"))
                .andExpect(jsonPath("$.email").value("shiva@example.com"));
    }
}
