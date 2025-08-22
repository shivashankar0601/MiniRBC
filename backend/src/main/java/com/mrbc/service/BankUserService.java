package com.mrbc.service;

import com.mrbc.model.BankAccount;
import com.mrbc.model.BankUser;
import com.mrbc.repository.BankUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankUserService {
    @Autowired
    private BankUserRepository bankUserRepository;

    @Autowired
    private BankAccountService bankAccountService;

    public BankUser createUser(BankUser bankUser) {
        if (bankUser.getAccounts() != null) {
            for (BankAccount account : bankUser.getAccounts()) {
                account.setBankUser(bankUser);
            }
        }
        return bankUserRepository.save(bankUser);
    }

    public List<BankUser> getAllUsers() {
        return bankUserRepository.findAll();
    }

    public BankUser getUserById(Long id) {
        return bankUserRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found with id: " + id));
    }
}