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

  @Override
  public UserDto register(User user) {
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

    if (user.isEmailAuthYn()) {
      result.setResult(false);
      result.setMessage("이미 인증 완료된 계정입니다.");
    } else {
      result.setResult(true);
      result.setMessage("이메일 인증이 완료되었습니다.");
    }
    user.setEmailAuthYn(true);
    user.setEmailAuthDateTime(LocalDateTime.now());

    userRepository.save(user);

    return result;
  }

  private void validateRegister(RegisterUserInput input) {
    if (userRepository.existsByEmail(input.getEmail())) {
      throw new MyException(ErrorCode.USER_ALREADY_EXIST);
    }
    if (!PasswordUtils.equalsPlainText(input.getPassword(), input.getPasswordCheck())) {
      throw new MyException(ErrorCode.PASSWORD_CHECK_INCORRECT);
    }
  }
}
