package com.example.fitnessrecord.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailAuthResult {
  private boolean result;
  private String message;

  private UserDto userDto;
}
