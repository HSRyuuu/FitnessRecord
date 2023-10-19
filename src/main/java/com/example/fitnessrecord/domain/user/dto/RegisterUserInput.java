package com.example.fitnessrecord.domain.user.dto;

import com.example.fitnessrecord.domain.user.persist.User;
import com.example.fitnessrecord.domain.user.type.UserType;
import com.example.fitnessrecord.global.util.PasswordUtils;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterUserInput {

  private String email;
  private String password;
  private String passwordCheck;
  private String nickname;

  public static User toEntity(RegisterUserInput input) {
    return User.builder()
        .email(input.getEmail())
        .password(PasswordUtils.encPassword(input.getPassword()))
        .nickname(input.getNickname())
        .emailAuthKey(UUID.randomUUID().toString())
        .userType(UserType.BASIC)
        .createdAt(LocalDateTime.now())
        .lastModifiedAt(LocalDateTime.now())
        .build();
  }

}
