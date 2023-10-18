package com.example.fitnessrecord.domain.user.dto;

import com.example.fitnessrecord.domain.user.persist.User;
import com.example.fitnessrecord.domain.user.type.UserType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
  private Long id;

  private String email;
  private String password;

  private String nickname;

  private boolean emailAuthYn;
  private LocalDateTime emailAuthDateTime;
  private String emailAuthKey;

  private UserType userType;

  private LocalDateTime createdAt;
  private LocalDateTime lastModifiedAt;

  public static UserDto fromEntity(User user){
    return UserDto.builder()
        .id(user.getId())
        .email(user.getEmail())
        .password(user.getPassword())
        .nickname(user.getNickname())
        .emailAuthYn(user.isEmailAuthYn())
        .emailAuthDateTime(user.getEmailAuthDateTime())
        .emailAuthKey(user.getEmailAuthKey())
        .userType(user.getUserType())
        .createdAt(user.getCreatedAt())
        .lastModifiedAt(user.getLastModifiedAt())
        .build();
  }





}
