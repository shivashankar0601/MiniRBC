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
import jakarta.persistence.Version;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @Version
    @JsonIgnore
    private Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank
    private String accountNumber;

    @Column(nullable = false)
    private String transitNumber = "03413";

    @Column(nullable = false)
    private String institutionNumber = "003";

    @NotNull
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @DecimalMin(value = "0.0")
    private BigDecimal balance;

    @JsonIgnore
    private boolean active = true;

    @ManyToOne
    @JoinColumn(name = "bank_user_id")
    @JsonIgnore
    private BankUser bankUser;
}