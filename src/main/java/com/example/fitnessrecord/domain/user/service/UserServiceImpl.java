package com.example.fitnessrecord.domain.user.service;

import com.example.fitnessrecord.domain.user.dto.EmailAuthResult;
import com.example.fitnessrecord.domain.user.dto.RegisterUserInput;
import com.example.fitnessrecord.domain.user.dto.UserDto;
import com.example.fitnessrecord.domain.user.persist.User;
import com.example.fitnessrecord.domain.user.persist.UserRepository;
import com.example.fitnessrecord.global.exception.ErrorCode;
import com.example.fitnessrecord.global.exception.MyException;
import com.example.fitnessrecord.global.util.PasswordUtils;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;


  @Override
  public boolean userExistsByEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  @Override
  public UserDto register(RegisterUserInput input) {
    validateRegister(input);
    User saved = userRepository.save(RegisterUserInput.toEntity(input));

    return UserDto.fromEntity(saved);
  }

  private void validateRegister(RegisterUserInput input) {
    if (userRepository.existsByEmail(input.getEmail())) {
      throw new MyException(ErrorCode.USER_ALREADY_EXIST);
    }
    if (!PasswordUtils.equalsPlainText(input.getPassword(), input.getPasswordCheck())) {
      throw new MyException(ErrorCode.PASSWORD_CHECK_INCORRECT);
    }
  }

  @Override
  public UserDto registerSocialUser(User user) {
    if (userRepository.existsByEmail(user.getEmail())) {
      throw new MyException(ErrorCode.USER_ALREADY_EXIST);
    }

    User saved = userRepository.save(user);
    return UserDto.fromEntity(saved);
  }

  @Override
  public UserDto findByEmail(String email) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new MyException(ErrorCode.USER_NOT_FOUND));
    return UserDto.fromEntity(user);
  }

  @Override
  public EmailAuthResult emailAuth(String uuid) {
    User user = userRepository.findByEmailAuthKey(uuid)
        .orElseThrow(() -> new MyException(ErrorCode.EMAIL_AUTH_KEY_ERROR));

    EmailAuthResult result = new EmailAuthResult();

    if (!Objects.isNull(user.getEmailAuthDateTime())) {
      //이미 인증된 경우
      result.setResult(true);
      result.setMessage("이미 인증 완료된 계정 입니다.");
    } else if (user.getEmailAuthDeadline().isBefore(LocalDateTime.now())) {
      //유효 기한이 지난 경우
      user.setEmailAuthDeadline(LocalDateTime.now().plusDays(1L));
      user.setEmailAuthKey(UUID.randomUUID().toString());
      userRepository.save(user);

      result.setResult(false);
      result.setMessage("이메일 인증 유효 기간이 만료 되었습니다. 이메일을 다시 발송했습니다.");
      result.setUserDto(UserDto.fromEntity(user));
    } else {
      //이메일 인증 완료
      result.setResult(true);
      result.setMessage("이메일 인증이 완료되었습니다.");

      user.setEmailAuthDateTime(LocalDateTime.now());
      userRepository.save(user);
    }

    return result;
  }

}
