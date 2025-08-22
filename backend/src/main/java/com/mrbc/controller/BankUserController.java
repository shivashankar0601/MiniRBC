package com.mrbc.controller;

import com.mrbc.model.BankUser;
import com.mrbc.service.BankUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class BankUserController {

    @Autowired
    private BankUserService bankUserService;

    @PostMapping
    public BankUser createUser(@RequestBody BankUser bankUser) {
        return bankUserService.createUser(bankUser);
    }

    @GetMapping
    public List<BankUser> getAllUsers(){
        return bankUserService.getAllUsers();
    }

    @GetMapping("/{id}")
    public BankUser getUserById(@PathVariable Long id){
        return bankUserService.getUserById(id);
    }
}