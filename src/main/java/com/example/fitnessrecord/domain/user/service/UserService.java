package com.example.fitnessrecord.domain.user.service;

import com.example.fitnessrecord.domain.user.dto.EmailAuthResult;
import com.example.fitnessrecord.domain.user.dto.UserDto;
import com.example.fitnessrecord.domain.user.dto.RegisterUserInput;

public interface UserService {

  /**
   * 이메일(UK)로 유저 존재 확인
   */
  boolean userExistsByEmail(String email);

  /**
   * 유저 회원가입
   */
  UserDto register(RegisterUserInput input);

  EmailAuthResult emailAuth(String uuid);

}
