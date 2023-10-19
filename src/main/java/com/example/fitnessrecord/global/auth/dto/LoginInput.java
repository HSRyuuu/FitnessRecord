package com.example.fitnessrecord.global.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginInput {
  private String email;
  private String password;
}
