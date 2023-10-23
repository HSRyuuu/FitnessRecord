package com.example.fitnessrecord.global.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.fitnessrecord.domain.user.dto.UserDto;
import com.example.fitnessrecord.domain.user.persist.User;
import com.example.fitnessrecord.domain.user.persist.UserRepository;
import com.example.fitnessrecord.domain.user.type.UserType;
import com.example.fitnessrecord.global.auth.dto.LoginInput;
import com.example.fitnessrecord.global.auth.sercurity.principal.PrincipalDetails;
import com.example.fitnessrecord.global.exception.ErrorCode;
import com.example.fitnessrecord.global.exception.MyException;
import com.example.fitnessrecord.global.util.PasswordUtils;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Slf4j
@SpringBootTest
@Transactional
class AuthServiceTest {

  @Autowired
  private AuthService authService;
  @Autowired
  private UserRepository userRepository;

  @Nested
  @DisplayName("유저 로그인 확인")
  class AuthenticateUser {

    String email = "test@test.com";
    String password = "test";

    @Test
    @DisplayName("성공")
    void authenticateUser() {
      //given
      User user = User.builder()
          .email(email)
          .password(PasswordUtils.encPassword(password))
          .emailAuthYn(true)
          .build();
      User saved = userRepository.save(user);
      LoginInput input = new LoginInput(email, password);

      //when
      UserDto authenticateUser = authService.authenticateUser(input);

      //then
      assertThat(authenticateUser.getId()).isEqualTo(saved.getId());
      assertThat(authenticateUser.getEmail()).isEqualTo(saved.getEmail());
    }

    @Test
    @DisplayName("실패 : 해당 email 유저가 존재하지 않을 때")
    void authenticateUser_LOGIN_FAILED_USER_NOT_FOUND() {
      //given
      User user = User.builder()
          .email(email)
          .password(PasswordUtils.encPassword(password))
          .emailAuthYn(true)
          .build();
      User saved = userRepository.save(user);
      LoginInput input = new LoginInput("xxx", password);

      //when
      //then
      try {
        authService.authenticateUser(input);
      } catch (MyException e) {
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.LOGIN_FAILED_USER_NOT_FOUND);
      }
    }

    @Test
    @DisplayName("실패 : 유저를 찾을 수 없을 때")
    void authenticateUser_LOGIN_FAILED_PASSWORD_INCORRECT() {
      //given
      User user = User.builder()
          .email(email)
          .password(PasswordUtils.encPassword(password))
          .emailAuthYn(true)
          .build();
      User saved = userRepository.save(user);
      LoginInput input = new LoginInput(email, "xxxx");

      //when
      //then
      try {
        authService.authenticateUser(input);
      } catch (MyException e) {
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.LOGIN_FAILED_PASSWORD_INCORRECT);
      }
    }

    @Test
    @DisplayName("실패 : 유저는 있지만, 이메일 인증이 안되어있을 때")
    void authenticateUser_EMAIL_AUTH_REQUIRED() {
      //given
      User user = User.builder()
          .email(email)
          .password(PasswordUtils.encPassword(password))
          .emailAuthYn(false)
          .build();
      userRepository.save(user);
      LoginInput input = new LoginInput(email, password);

      //when
      //then
      try {
        authService.authenticateUser(input);
      } catch (MyException e) {
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.EMAIL_AUTH_REQUIRED);
      }
    }

  }

  @Nested
  @DisplayName("loadUserByUsername")
  class LoadUserByUsername {

    @Test
    @DisplayName("성공")
    void loadUserByUsername() {
      //given

      String email = "test@test.com";
      User user = User.builder()
          .email(email)
          .password(PasswordUtils.encPassword("test"))
          .emailAuthYn(true)
          .userType(UserType.BASIC)
          .build();
      User saved = userRepository.save(user);

      //when
      PrincipalDetails principalDetails =
          (PrincipalDetails) authService.loadUserByUsername(email);

      //then
      assertThat(principalDetails.getUserId()).isEqualTo(saved.getId());
      assertThat(principalDetails.getUsername()).isEqualTo(email);
    }

    @Test
    @DisplayName("실패 : 회원이 존재하지 않음")
    void loadUserByUsername_UsernameNotFoundException() {
      //given
      String email = "test@test.com";
      User user = User.builder()
          .email("xxx")
          .password(PasswordUtils.encPassword("test"))
          .emailAuthYn(true)
          .userType(UserType.BASIC)
          .build();
      userRepository.save(user);

      //when
      assertThatThrownBy(() -> authService.loadUserByUsername(email))
          .isInstanceOf(UsernameNotFoundException.class);
    }
  }


}