package com.mrbc.exceptions;

/**
 * Insufficient funds exception to be used when an account has insufficient funds for a transaction.
 */
public class InsufficientFundsException extends RuntimeException {
  public InsufficientFundsException(String message) {
    super(message);
  }
}
