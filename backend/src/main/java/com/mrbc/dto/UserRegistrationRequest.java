package com.mrbc.dto;

import lombok.Data;

@Data
public class UserRegistrationRequest {
  private String name;
  private String email;
  private String phoneNumber;
  private String address;
  private String password;
}
