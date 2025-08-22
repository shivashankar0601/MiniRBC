package com.mrbc.repository;

import com.mrbc.model.BankUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * User repository interface for CRUD operations on User entities.
 * Provides methods to find users by email and phone number.
 */
public interface BankUserRepository extends JpaRepository<BankUser, Long> {
    Optional<BankUser> findByEmail(String email);

    Optional<BankUser> findByPhoneNumber(String phoneNumber);
}
