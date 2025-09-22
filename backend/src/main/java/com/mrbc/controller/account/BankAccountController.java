package com.mrbc.controller.account;

import com.mrbc.model.BankAccount;
import com.mrbc.service.account.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class BankAccountController {

    @Autowired
    private BankAccountService bankAccountService;

    @PostMapping("/create-account")
    public BankAccount createAccount(@RequestBody Long userId,
                                     @RequestBody BankAccount bankAccount) {
        return bankAccountService.createAccountForUser(userId, bankAccount);
    }

    @GetMapping("/user/{userId}")
    public List<BankAccount> getAccountsByUserId(@PathVariable Long userId) {
        return bankAccountService.getBankAccountByUserId(userId);
    }

    @PostMapping("/deposit")
    public BankAccount deposit(@RequestParam Long accountId,
                               @RequestParam BigDecimal amount) {
        return bankAccountService.deposit(accountId, amount);
    }

    @PostMapping("/withdraw")
    public BankAccount withdraw(@RequestParam Long accountId,
                                @RequestParam BigDecimal amount) {
        return bankAccountService.withdraw(accountId, amount);
    }

    @PostMapping("/transfer")
    public String transfer(@RequestParam Long fromAccountId,
                           @RequestParam Long toAccountId,
                           @RequestParam BigDecimal amount) {
        bankAccountService.transfer(fromAccountId, toAccountId, amount);
        return "Transfer successful";
    }

    @GetMapping("/all")
    public List<BankAccount> getAllAccounts() {
        return bankAccountService.getAllAccounts();
    }
}