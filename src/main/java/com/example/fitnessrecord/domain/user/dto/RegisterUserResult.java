package com.example.fitnessrecord.domain.user.dto;

import com.example.fitnessrecord.domain.user.type.UserType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class RegisterUserResult {
  private String email;
  private String nickname;
  private LocalDateTime createdAt;
  private UserType userType;

  public static RegisterUserResult fromDto(UserDto userDto){
    return RegisterUserResult.builder()
        .email(userDto.getEmail())
        .nickname(userDto.getNickname())
        .createdAt(userDto.getCreatedAt())
        .userType(userDto.getUserType())
        .build();
  }


}
