package com.mrbc.service.account;

import com.mrbc.exceptions.InsufficientFundsException;
import com.mrbc.exceptions.ResourceNotFoundException;
import com.mrbc.model.BankAccount;
import com.mrbc.model.BankUser;
import com.mrbc.repository.account.BankAccountRepository;
import com.mrbc.repository.user.BankUserRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BankAccountService {

  private final BankUserRepository bankUserRepository;

  private final BankAccountRepository bankAccountRepository;

  public BankAccountService(
      BankUserRepository bankUserRepository, BankAccountRepository bankAccountRepository) {
    this.bankUserRepository = bankUserRepository;
    this.bankAccountRepository = bankAccountRepository;
  }

  public BankAccount createAccountForUser(Long userId, BankAccount account) {
    BankUser user =
        bankUserRepository
            .findById(userId)
            .orElseThrow(
                () -> new ResourceNotFoundException("User with ID " + userId + " not found"));

    // Associate the account with the user
    account.setBankUser(user);

    // Save the account (cascading is optional depending on your setup)
    return bankAccountRepository.save(account);
  }

  public List<BankAccount> getBankAccountByUserId(Long userId) {
    return bankAccountRepository.findByBankUserId(userId);
  }

  @Transactional
  public BankAccount deposit(Long accountId, BigDecimal amount) {
    BankAccount account =
        bankAccountRepository
            .findByIdForUpdate(accountId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Account with ID " + accountId + " not found"));

    // Update the balance
    account.setBalance(account.getBalance().add(amount));

    // Save the updated account
    return bankAccountRepository.save(account);
  }

  @Transactional
  public BankAccount withdraw(Long accountId, BigDecimal amount) {
    BankAccount account =
        bankAccountRepository
            .findByIdForUpdate(accountId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Account with ID " + accountId + " not found"));

    // Check if the account has sufficient funds
    if (account.getBalance().compareTo(amount) < 0) {
      throw new InsufficientFundsException(
          "Insufficient funds for withdrawal in account " + accountId);
    }

    // Update the balance
    account.setBalance(account.getBalance().subtract(amount));

    // Save the updated account
    return bankAccountRepository.save(account);
  }

  @Transactional
  public BankAccount transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {

    if (fromAccountId.equals(toAccountId)) {
      throw new IllegalArgumentException("Cannot transfer to the same account");
    }

    BankAccount bankAccount;
    if (fromAccountId < toAccountId) {
      bankAccount = withdraw(fromAccountId, amount);
      deposit(toAccountId, amount);
    } else {
      deposit(toAccountId, amount);
      bankAccount = withdraw(fromAccountId, amount);
    }

    // Return the updated account after transfer
    return bankAccount;
  }

  @Transactional
  public List<BankAccount> getAllAccounts() {
    return bankAccountRepository.findAll();
  }
}
