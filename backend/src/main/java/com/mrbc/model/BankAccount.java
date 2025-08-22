package com.mrbc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(unique = true, nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    @JsonIgnore
    private String transitNumber = "03413";

    @Column(nullable = false)
    @JsonIgnore
    private String institutionNumber = "003";

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    private BigDecimal balance;

    @JsonIgnore
    private boolean active;

    @ManyToOne
    @JoinColumn(name="bankUser")
    @JsonIgnore
    private BankUser bankUser;
}