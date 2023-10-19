package com.example.fitnessrecord.global.auth.service;


import com.example.fitnessrecord.domain.user.dto.UserDto;
import com.example.fitnessrecord.domain.user.persist.User;
import com.example.fitnessrecord.domain.user.persist.UserRepository;
import com.example.fitnessrecord.global.auth.dto.LoginInput;
import com.example.fitnessrecord.global.auth.sercurity.principal.PrincipalDetails;
import com.example.fitnessrecord.global.exception.ErrorCode;
import com.example.fitnessrecord.global.exception.MyException;
import com.example.fitnessrecord.global.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService implements UserDetailsService {

  private final UserRepository userRepository;

  public UserDto authenticateUser(LoginInput input) {
    User user = userRepository.findByEmail(input.getEmail())
        .orElseThrow(() -> new MyException(ErrorCode.LOGIN_FAILED_USER_NOT_FOUND));
    if (!PasswordUtils.equalsPlainTextAndHashed(input.getPassword(), user.getPassword())) {
      throw new MyException(ErrorCode.LOGIN_FAILED_PASSWORD_INCORRECT);
    }
    if (!user.isEmailAuthYn()) {
      throw new MyException(ErrorCode.EMAIL_AUTH_REQUIRED);
    }
    return UserDto.fromEntity(user);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException("회원 정보가 존재하지 않습니다."));
    log.info("로그인 성공[ ID : {} ]", user.getEmail());

    return new PrincipalDetails(user);
  }
}
