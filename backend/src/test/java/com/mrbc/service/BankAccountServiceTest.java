package com.mrbc.service;

import com.mrbc.model.AccountType;
import com.mrbc.model.BankAccount;
import com.mrbc.model.BankUser;
import com.mrbc.repository.BankAccountRepository;
import com.mrbc.repository.BankUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BankAccountServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @InjectMocks
    private BankAccountService bankAccountService;

    @InjectMocks
    private BankUserService bankUserService;

    @Mock
    private BankUserRepository bankUserRepository;

    @Test
    void deposit_shouldIncreaseBalance_whenAccountExists() {

        BigDecimal initialBalance = BigDecimal.valueOf(100);

        var bankUser = new BankUser();
        bankUser.setName("shiva");
        bankUser.setEmail("shiva@gmail.com");
        bankUser.setAddress("426 aylesworth");
        bankUser.setPhoneNumber("9879898585");
        bankUser.setAccounts(List.of(BankAccount.builder().accountNumber("112233").balance(initialBalance).accountType(AccountType.CHECKING).build()));
        bankUser = bankUserService.createUser(bankUser);
        List<BankAccount> accounts = bankUser.getAccounts();
        var accountId = accounts.get(0).getId();
        BankAccount mockAccount = BankAccount.builder()
                .id(accountId)
                .balance(initialBalance)
                .build();

        mockAccount = bankAccountService.createAccountForUser(bankUser.getId(), mockAccount);

        BigDecimal depositAmount = BigDecimal.valueOf(50);

        when(bankAccountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));
        when(bankAccountRepository.save(any(BankAccount.class))).thenAnswer(i -> i.getArgument(0));

        BankAccount result = bankAccountService.deposit(accountId, depositAmount);

        assertEquals(BigDecimal.valueOf(150), result.getBalance());
    }

    @Test
    void testCreateUserWithoutAccounts() {
        // Prepare data
        BankUser user = new BankUser();
        user.setName("Shiva");
        user.setEmail("shiva@example.com");
        user.setPhoneNumber("9876543210");
        user.setAddress("Somewhere");

        when(bankUserRepository.save(any())).thenReturn(user);

        // Call method
        BankUser savedUser = bankUserService.createUser(user);

        // Verify
        assertEquals("Shiva", savedUser.getName());
        verify(bankUserRepository).save(user);
    }

}

