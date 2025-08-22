package com.mrbc.service;

import com.mrbc.model.BankAccount;
import com.mrbc.model.BankUser;
import com.mrbc.repository.BankAccountRepository;
import com.mrbc.repository.BankUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankAccountService {

    @Autowired
    private BankUserRepository bankUserRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    public BankAccount createAccountForUser(Long userId, BankAccount account) {
        BankUser user = bankUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));

        // Associate the account with the user
        account.setBankUser(user);

        // Save the account (cascading is optional depending on your setup)
        return bankAccountRepository.save(account);
    }
}
