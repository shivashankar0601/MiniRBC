package com.mrbc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrbc.model.BankUser;
import com.mrbc.service.BankUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BankAccountControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BankUserService bankUserService;

    @InjectMocks
    private com.mrbc.controller.BankUserController bankUserController;

    private ObjectMapper objectMapper;

    private BankUser sampleUser;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(bankUserController).build();
        objectMapper = new ObjectMapper();

        sampleUser = BankUser.builder()
                .id(1L)
                .name("Shiva Shankar")
                .email("shiva@example.com")
                .phoneNumber("1234567890")
                .address("Example address, Scarborough")
                .accounts(null)
                .build();
    }

    @Test
    void testCreateUser() throws Exception {
        when(bankUserService.createUser(any(BankUser.class))).thenReturn(sampleUser);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleUser.getId()))
                .andExpect(jsonPath("$.name").value(sampleUser.getName()))
                .andExpect(jsonPath("$.email").value(sampleUser.getEmail()));

        verify(bankUserService, times(1)).createUser(any(BankUser.class));
    }

    @Test
    void testGetAllUsers() throws Exception {
        List<BankUser> users = Collections.singletonList(sampleUser);
        when(bankUserService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(sampleUser.getId()))
                .andExpect(jsonPath("$[0].email").value(sampleUser.getEmail()));

        verify(bankUserService, times(1)).getAllUsers();
    }

    @Test
    void testGetUserById() throws Exception {
        when(bankUserService.getUserById(1L)).thenReturn(sampleUser);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleUser.getId()))
                .andExpect(jsonPath("$.email").value(sampleUser.getEmail()));

        verify(bankUserService, times(1)).getUserById(1L);
    }
}