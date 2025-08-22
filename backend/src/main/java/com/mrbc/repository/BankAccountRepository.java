package com.mrbc.repository;

import com.mrbc.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Bank account repository interface for CRUD operations on BankAccount entities.
 */
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    List<BankAccount> findByBankUserId(Long id);
}
