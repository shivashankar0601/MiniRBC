package com.mrbc.service;

import com.mrbc.exceptions.ResourceNotFoundException;
import com.mrbc.model.BankAccount;
import com.mrbc.model.BankUser;
import com.mrbc.repository.user.BankUserRepository;
import com.mrbc.service.account.BankAccountService;
import com.mrbc.service.user.BankUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BankUserServiceTest {

    @Mock
    private BankUserRepository bankUserRepository;

    @Mock
    private BankAccountService bankAccountService;

    @InjectMocks
    private BankUserService bankUserService;

    private BankUser sampleUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleUser = BankUser.builder()
                .id(1L)
                .name("Shiva Shankar")
                .email("shiva@example.com")
                .phoneNumber("1234567890")
                .address("426 Aylesworth Ave")
                .accounts(new ArrayList<>())
                .build();
    }

    @Test
    void testCreateUserWithoutAccounts() {
        when(bankUserRepository.save(any(BankUser.class))).thenReturn(sampleUser);

        BankUser result = bankUserService.createUser(sampleUser);

        assertNotNull(result);
        assertEquals("Shiva Shankar", result.getName());
        verify(bankUserRepository, times(1)).save(sampleUser);
    }

    @Test
    void testCreateUserWithAccounts() {
        BankAccount account = new BankAccount();
        sampleUser.setAccounts(List.of(account));

        when(bankUserRepository.save(any(BankUser.class))).thenReturn(sampleUser);

        BankUser result = bankUserService.createUser(sampleUser);

        assertNotNull(result);
        verify(bankUserRepository).save(sampleUser);
        assertEquals(sampleUser, account.getBankUser());
    }

    @Test
    void testGetAllUsers() {
        List<BankUser> userList = List.of(sampleUser);
        when(bankUserRepository.findAll()).thenReturn(userList);

        List<BankUser> result = bankUserService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals(sampleUser.getEmail(), result.get(0).getEmail());
    }

    @Test
    void testGetUserById_Found() {
        when(bankUserRepository.findById(1L)).thenReturn(Optional.of(sampleUser));

        BankUser result = bankUserService.getUserById(1L);

        assertEquals("Shiva Shankar", result.getName());
        verify(bankUserRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserById_NotFound() {
        when(bankUserRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                bankUserService.getUserById(99L));

        assertTrue(exception.getMessage().contains("User not found with id"));
    }
}
