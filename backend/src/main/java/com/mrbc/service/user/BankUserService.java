package com.mrbc.service.user;

import com.mrbc.exceptions.ResourceNotFoundException;
import com.mrbc.model.BankAccount;
import com.mrbc.model.BankUser;
import com.mrbc.repository.user.BankUserRepository;
import com.mrbc.service.account.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankUserService {
    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private BankUserRepository bankUserRepository;

    public BankUser createUser(BankUser bankUser) {
        if (bankUser.getAccounts() != null) {
            for (BankAccount account : bankUser.getAccounts()) {
//                var createdAccount = bankAccountService.createAccountForUser(bankUser.getId(), account);
//                createdAccount.setBankUser(bankUser);
                account.setBankUser(bankUser);
            }
        }
        return bankUserRepository.save(bankUser);
    }

    public List<BankUser> getAllUsers() {
        return bankUserRepository.findAll();
    }

    public BankUser getUserById(Long id) {
        return bankUserRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }
}