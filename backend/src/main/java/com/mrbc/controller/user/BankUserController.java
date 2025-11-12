package com.mrbc.controller.user;

import com.mrbc.model.BankUser;
import com.mrbc.service.user.BankUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class BankUserController {

    private final BankUserService bankUserService;

    @PostMapping
    public ResponseEntity<BankUser> createUser(@RequestBody BankUser bankUser) {
        return ResponseEntity.ok(bankUserService.createUser(bankUser));
    }

    @GetMapping
    public ResponseEntity<List<BankUser>> getAllUsers() {
        return ResponseEntity.ok(bankUserService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankUser> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(bankUserService.getUserById(id));
    }
}
