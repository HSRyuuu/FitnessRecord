package com.example.fitnessrecord.global.auth.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
  private String userId;
  private String nickname;
  private String token;
}
