package com.example.fitnessrecord.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.fitnessrecord.domain.user.dto.EmailAuthResult;
import com.example.fitnessrecord.domain.user.dto.RegisterUserInput;
import com.example.fitnessrecord.domain.user.dto.UserDto;
import com.example.fitnessrecord.domain.user.persist.User;
import com.example.fitnessrecord.domain.user.persist.UserRepository;
import com.example.fitnessrecord.global.exception.ErrorCode;
import com.example.fitnessrecord.global.exception.MyException;
import com.example.fitnessrecord.global.util.PasswordUtils;
import java.time.LocalDateTime;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@Slf4j
@SpringBootTest
@Transactional
class UserServiceTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserService userService;

  @Test
  @DisplayName("이메일로 유저 존재 여부 확인")
  void userExistsByEmail() {
    //given
    User user = User.builder()
        .email("test@test.com")
        .build();
    userRepository.save(user);

    //when
    boolean result = userService.userExistsByEmail(user.getEmail());

    //then
    assertThat(result).isTrue();
  }

  @Nested
  @DisplayName("유저 회원가입")
  class Register {

    @Test
    @DisplayName("성공")
    void register() {
      //given
      RegisterUserInput input = RegisterUserInput.builder()
          .email("test@test.com")
          .password("test_pw")
          .passwordCheck("test_pw")
          .nickname("test_nickname")
          .build();

      //when
      UserDto registered = userService.register(input);

      boolean passwordEquals = PasswordUtils.equalsPlainTextAndHashed(
          input.getPassword(), registered.getPassword());

      //then
      assertThat(registered.getEmail()).isEqualTo(input.getEmail());
      assertThat(passwordEquals).isTrue();
    }

    @Test
    @DisplayName("실패 : 이미 존재하는 회원")
    void register_USER_ALREADY_EXIST() {
      //given
      User user = User.builder()
          .email("test@test.com")
          .build();
      userRepository.save(user);

      RegisterUserInput input = RegisterUserInput.builder()
          .email("test@test.com")
          .password("test_pw")
          .passwordCheck("test_pw")
          .nickname("test_nickname")
          .build();

      //when
      //then
      try {
        userService.register(input);
      } catch (MyException e) {
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.USER_ALREADY_EXIST);
      }
    }

    @Test
    @DisplayName("실패 : 비밀번호 확인 불일치")
    void register_PASSWORD_CHECK_INCORRECT() {
      //given
      RegisterUserInput input = RegisterUserInput.builder()
          .email("test@test.com")
          .password("test_pw")
          .passwordCheck("test_pw!!!!!!!")
          .nickname("test_nickname")
          .build();

      //when
      //then
      try {
        userService.register(input);
      } catch (MyException e) {
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.PASSWORD_CHECK_INCORRECT);
      }
    }

  }

  @Nested
  @DisplayName("소셜 로그인 시 직접 회원 가입")
  class RegisterDirectly {

    @Test
    @DisplayName("성공")
    void registerDirectly() {
      //given
      User user = User.builder()
          .email("test@test.com")
          .build();

      //when
      UserDto userDto = userService.registerSocialUser(user);
      User findUser = userRepository.findById(userDto.getId()).get();
      //then
      assertThat(findUser.getEmail()).isEqualTo(userDto.getEmail());
    }

    @Test
    @DisplayName("실패 : 이미 존재하는 유저")
    void registerDirectly_USER_ALREADY_EXIST() {
      //given
      User user = User.builder()
          .email("test@test.com")
          .build();
      userRepository.save(user);

      User newUser = User.builder()
          .email("test@test.com")
          .build();

      //when
      //then
      try {
        userService.registerSocialUser(newUser);
      } catch (MyException e) {
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.USER_ALREADY_EXIST);
      }
    }
  }

  @Nested
  @DisplayName("이메일로 유저 찾기")
  class FindByEmail {

    @Test
    @DisplayName("성공")
    void findByEmail() {
      //given
      User user = User.builder()
          .email("test@test.com")
          .build();
      userRepository.save(user);

      //when
      UserDto findUser = userService.findByEmail(user.getEmail());

      //then
      assertThat(user.getEmail()).isEqualTo(findUser.getEmail());
    }

    @Test
    @DisplayName("실패 : 존재하지 않는 유저")
    void findByEmail_USER_NOT_FOUND() {
      //given
      User user = User.builder()
          .email("test@test.com")
          .build();
      userRepository.save(user);

      //when
      //then
      try {
        userService.findByEmail("xxxxxxx");
      } catch (MyException e) {
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
      }
    }
  }

  @Nested
  @DisplayName("이메일 인증")
  class EmailAuth {

    @Test
    @DisplayName("성공")
    void emailAuth() {
      //given
      String emailAuthKey = "hello";
      User user = User.builder()
          .email("test@test.com")
          .emailAuthKey(emailAuthKey)
          .emailAuthDeadline(LocalDateTime.now().plusDays(1L))
          .build();
      userRepository.save(user);

      //when
      EmailAuthResult result = userService.emailAuth(emailAuthKey);

      //then
      assertThat(result.isResult()).isTrue();
      assertThat(result.getMessage()).isEqualTo("이메일 인증이 완료되었습니다.");
    }

    @Test
    @DisplayName("성공 - 이미 인증 완료된 계정")
    void emailAuth_already_auth() {
      //given
      String emailAuthKey = "hello";
      User user = User.builder()
          .email("test@test.com")
          .emailAuthKey(emailAuthKey)
          .emailAuthDateTime(LocalDateTime.now())
          .build();
      userRepository.save(user);

      //when
      EmailAuthResult result = userService.emailAuth(emailAuthKey);

      //then
      assertThat(result.isResult()).isTrue();
      assertThat(result.getMessage()).isEqualTo("이미 인증 완료된 계정입니다.");
    }

    @Test
    @DisplayName("실패 :이메일 유효기한 만료")
    void emailAuth_deadline_passed() {
      //given
      String emailAuthKey = "hello";
      User user = User.builder()
          .email("test@test.com")
          .emailAuthKey(emailAuthKey)
          .emailAuthDeadline(LocalDateTime.now().minusDays(1L))
          .build();
      userRepository.save(user);

      //when
      EmailAuthResult result = userService.emailAuth(emailAuthKey);

      //then
      assertThat(result.isResult()).isFalse();
      assertThat(result.getMessage()).isEqualTo("이메일 인증 유효 기간이 만료 되었습니다. 이메일을 다시 발송했습니다.");
      assertThat(result.getUserDto()).isNotNull();
    }

    @Test
    @DisplayName("실패: 이메일 인증 키 문제")
    void emailAuth_EMAIL_AUTH_KEY_ERROR() {
      //given
      String emailAuthKey = "hello";
      String wrongKey = "wrong";
      User user = User.builder()
          .email("test@test.com")
          .emailAuthYn(false)
          .emailAuthKey(emailAuthKey)
          .build();
      userRepository.save(user);

      //when
      //then
      try {
        userService.emailAuth(wrongKey);
      } catch (MyException e) {
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.EMAIL_AUTH_KEY_ERROR);
      }
    }
  }

}