package com.mrbc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrbc.model.BankUser;
import com.mrbc.service.BankUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
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
class BankUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BankUserService bankUserService;

    @Autowired
    private ObjectMapper objectMapper;

    private BankUser user;

    @BeforeEach
    void setUp() {
        user = BankUser.builder()
                .id(1L)
                .name("Shiva Shankar")
                .email("shiva@example.com")
                .phoneNumber("1234567890")
                .address("Example address, Scarborough")
                .build();
    }

    @Test
    void testCreateUser() throws Exception {
        when(bankUserService.createUser(Mockito.any(BankUser.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Shiva Shankar"))
                .andExpect(jsonPath("$.email").value("shiva@example.com"));
    }

    @Test
    void testGetAllUsers() throws Exception {
        when(bankUserService.getAllUsers()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].email").value("shiva@example.com"));
    }

    @Test
    void testGetUserById() throws Exception {
        when(bankUserService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Shiva Shankar"))
                .andExpect(jsonPath("$.email").value("shiva@example.com"));
    }
}