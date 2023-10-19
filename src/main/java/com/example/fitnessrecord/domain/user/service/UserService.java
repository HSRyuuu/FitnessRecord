package com.example.fitnessrecord.domain.user.service;

import com.example.fitnessrecord.domain.user.dto.EmailAuthResult;
import com.example.fitnessrecord.domain.user.dto.UserDto;
import com.example.fitnessrecord.domain.user.dto.RegisterUserInput;
import com.example.fitnessrecord.domain.user.persist.User;

public interface UserService {

  /**
   * 이메일(UK)로 유저 존재 확인
   */
  boolean userExistsByEmail(String email);

  /**
   * 유저 회원 가입
   */
  UserDto register(RegisterUserInput input);

  /**
   * 유저 회원 가입 2
   */
  UserDto register(User user);

  /**
   * find user by email
   */
  UserDto findByEmail(String email);
  /**
   * 이메일 인증 처리
   */
  EmailAuthResult emailAuth(String uuid);

}
