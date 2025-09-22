package com.mrbc.service;

import com.mrbc.model.AccountType;
import com.mrbc.model.BankAccount;
import com.mrbc.model.BankUser;
import com.mrbc.repository.account.BankAccountRepository;
import com.mrbc.repository.user.BankUserRepository;
import com.mrbc.service.account.BankAccountService;
import com.mrbc.service.user.BankUserService;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BankAccountServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private BankUserRepository bankUserRepository;

    @InjectMocks
    private BankAccountService bankAccountService;

    @InjectMocks
    private BankUserService bankUserService;

    @Test
    void deposit_shouldIncreaseBalance_whenAccountExists() {
        // Arrange
        BigDecimal initialBalance = BigDecimal.valueOf(100);
        BigDecimal depositAmount = BigDecimal.valueOf(50);
        BigDecimal expectedBalance = initialBalance.add(depositAmount);

        // Create test data
        BankUser bankUser = BankUser.builder()
                .id(1L)
                .name("Shiva")
                .email("shiva@gmail.com")
                .address("426 Aylesworth")
                .phoneNumber("9879898585")
                .build();

        BankAccount account = BankAccount.builder()
                .id(10L)
                .accountNumber("112233")
                .balance(initialBalance)
                .accountType(AccountType.CHECKING)
                .build();

        bankUser.setAccounts(List.of(account));

        // Mock repository behavior
        when(bankUserRepository.save(any(BankUser.class))).thenReturn(bankUser);
        when(bankUserRepository.findById(bankUser.getId())).thenReturn(Optional.of(bankUser));

        // Simulate save generating the ID
        when(bankAccountRepository.save(any(BankAccount.class))).thenAnswer(invocation -> {
            BankAccount acc = invocation.getArgument(0);
            acc.setId(10L);
            return acc;
        });

        when(bankAccountRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(account));

        // Act
        BankUser createdUser = bankUserService.createUser(bankUser);
        BankAccount createdAccount = bankAccountService.createAccountForUser(createdUser.getId(), account);

        // Deposit action
        BankAccount result = bankAccountService.deposit(createdAccount.getId(), depositAmount);

        // Assert
        assertEquals(expectedBalance, result.getBalance());
    }

    @Test
    void withdraw_shouldDecreaseBalance_whenSufficientFunds() {
        // Arrange
        BigDecimal initialBalance = BigDecimal.valueOf(100);
        BigDecimal withdrawlAmount = BigDecimal.valueOf(50);
        BigDecimal expectedBalance = initialBalance.subtract(withdrawlAmount);

        // Create test data
        BankUser bankUser = BankUser.builder()
                .id(1L)
                .name("Shiva")
                .email("shiva@gmail.com")
                .address("426 Aylesworth")
                .phoneNumber("9879898585")
                .build();

        BankAccount account = BankAccount.builder()
                .id(10L)
                .accountNumber("112233")
                .balance(initialBalance)
                .accountType(AccountType.CHECKING)
                .build();

        bankUser.setAccounts(List.of(account));

        // Mock repository behavior
        when(bankUserRepository.save(any(BankUser.class))).thenReturn(bankUser);
        when(bankUserRepository.findById(bankUser.getId())).thenReturn(Optional.of(bankUser));

        // Simulate save generating the ID
        when(bankAccountRepository.save(any(BankAccount.class))).thenAnswer(invocation -> {
            BankAccount acc = invocation.getArgument(0);
            acc.setId(10L);
            return acc;
        });

        when(bankAccountRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(account));

        // Act
        BankUser createdUser = bankUserService.createUser(bankUser);
        BankAccount createdAccount = bankAccountService.createAccountForUser(createdUser.getId(), account);

        // Deposit action
        BankAccount result = bankAccountService.withdraw(createdAccount.getId(), withdrawlAmount);

        // Assert
        assertEquals(expectedBalance, result.getBalance());
    }

    @Test
    void transfer_shouldMoveFundsBetweenAccounts_whenSufficientFunds() {
        // Arrange
        BigDecimal initialBalanceFrom = BigDecimal.valueOf(100);
        BigDecimal initialBalanceTo = BigDecimal.valueOf(50);
        BigDecimal transferAmount = BigDecimal.valueOf(30);
        BigDecimal expectedBalanceFrom = initialBalanceFrom.subtract(transferAmount);
        BigDecimal expectedBalanceTo = initialBalanceTo.add(transferAmount);

        // Create test data
        BankUser fromBankUser = BankUser.builder()
                .id(1L)
                .name("Shiva")
                .email("shiva@gmail.com")
                .address("426 Aylesworth")
                .phoneNumber("9879898585")
                .build();

        BankAccount fromAccount = BankAccount.builder()
                .id(10L)
                .accountNumber("112233")
                .balance(initialBalanceFrom)
                .accountType(AccountType.CHECKING)
                .build();

        fromBankUser.setAccounts(List.of(fromAccount));

        BankUser toBankUser = BankUser.builder()
                .id(2L)
                .name("Vishnu")
                .email("vishnu@gmail.com")
                .address("123 Main Street")
                .phoneNumber("9876543210")
                .build();

        BankAccount toAccount = BankAccount.builder()
                .id(20L)
                .accountNumber("445566")
                .balance(initialBalanceTo)
                .accountType(AccountType.CHECKING)
                .build();

        toBankUser.setAccounts(List.of(toAccount));

        // Mock repository behavior
        when(bankUserRepository.save(any(BankUser.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(bankUserRepository.findById(1L)).thenReturn(Optional.of(fromBankUser));
        when(bankUserRepository.findById(2L)).thenReturn(Optional.of(toBankUser));

        when(bankAccountRepository.save(any(BankAccount.class)))
                .thenAnswer(invocation -> {
                    BankAccount acc = invocation.getArgument(0);
                    if (acc.getId() == null) {
                        acc.setId(acc == fromAccount ? 10L : 20L);
                    }
                    return acc;
                });

        when(bankAccountRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(fromAccount));
        when(bankAccountRepository.findByIdForUpdate(20L)).thenReturn(Optional.of(toAccount));

        // Act
        BankUser createdFromUser = bankUserService.createUser(fromBankUser);
        BankAccount createdFromAccount = bankAccountService.createAccountForUser(createdFromUser.getId(), fromAccount);

        BankUser createdToUser = bankUserService.createUser(toBankUser);
        BankAccount createdToAccount = bankAccountService.createAccountForUser(createdToUser.getId(), toAccount);

        // Transfer action
        BankAccount resultFrom = bankAccountService.withdraw(createdFromAccount.getId(), transferAmount);
        BankAccount resultTo = bankAccountService.deposit(createdToAccount.getId(), transferAmount);

        // Assert
        assertEquals(expectedBalanceFrom, resultFrom.getBalance());
        assertEquals(expectedBalanceTo, resultTo.getBalance());
    }

    @Test
    void testCreateUserWithoutAccounts() {
        BankUser user = new BankUser();
        user.setName("Shiva");
        user.setEmail("shiva@example.com");
        user.setPhoneNumber("9876543210");
        user.setAddress("Somewhere");

        when(bankUserRepository.save(any())).thenReturn(user);

        BankUser savedUser = bankUserService.createUser(user);

        assertEquals("Shiva", savedUser.getName());
        verify(bankUserRepository).save(user);
    }
}