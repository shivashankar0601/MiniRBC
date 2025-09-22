package com.mrbc.service.auth;

import com.mrbc.dto.AuthResponse;
import com.mrbc.dto.AuthRequest;
import com.mrbc.dto.UserRegistrationRequest;
import com.mrbc.model.BankUser;
import com.mrbc.repository.user.BankUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final BankUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public BankUser register(UserRegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        BankUser user = BankUser.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .password(passwordEncoder.encode(request.getPassword())) // hash!
                .role("ROLE_USER")
                .build();

        return userRepository.save(user);
    }

    public AuthResponse login(AuthRequest request) {
        BankUser user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }
}

