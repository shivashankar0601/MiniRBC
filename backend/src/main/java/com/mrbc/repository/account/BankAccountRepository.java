package com.mrbc.repository.account;

import com.mrbc.model.BankAccount;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/** Bank account repository interface for CRUD operations on BankAccount entities. */
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
  List<BankAccount> findByBankUserId(Long id);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT a FROM BankAccount a WHERE a.id = :id")
  Optional<BankAccount> findByIdForUpdate(@Param("id") Long id);
}
