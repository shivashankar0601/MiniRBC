package com.mrbc.repository.user;

import com.mrbc.model.BankUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * User repository interface for CRUD operations on User entities. Provides methods to find users by
 * email and phone number.
 */
public interface BankUserRepository extends JpaRepository<BankUser, Long> {
  Optional<BankUser> findByEmail(String email);

  Optional<BankUser> findByPhoneNumber(String phoneNumber);

  boolean existsByEmail(String email);
}
