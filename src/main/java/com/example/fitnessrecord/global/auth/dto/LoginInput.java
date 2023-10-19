package com.example.fitnessrecord.global.auth.dto;

import lombok.Data;

@Data
public class LoginInput {
  private String email;
  private String password;
}
