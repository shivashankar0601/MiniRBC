package com.mrbc.controller.auth;

import com.mrbc.dto.AuthRequest;
import com.mrbc.dto.AuthResponse;
import com.mrbc.dto.UserRegistrationRequest;
import com.mrbc.model.BankUser;
import com.mrbc.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<BankUser> register(@RequestBody @Valid UserRegistrationRequest request) {
    BankUser user = authService.register(request);
    return ResponseEntity.ok(user);
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthRequest request) {
    return ResponseEntity.ok(authService.login(request));
  }
}
