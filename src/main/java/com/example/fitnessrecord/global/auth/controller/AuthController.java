package com.example.fitnessrecord.global.auth.controller;

import com.example.fitnessrecord.domain.user.dto.UserDto;
import com.example.fitnessrecord.global.auth.dto.AuthResponse;
import com.example.fitnessrecord.global.auth.dto.LoginInput;
import com.example.fitnessrecord.global.auth.sercurity.jwt.TokenProvider;
import com.example.fitnessrecord.global.auth.service.AuthService;
import com.example.fitnessrecord.global.util.GrantUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {

  private final AuthService authService;
  private final TokenProvider tokenProvider;

  @PostMapping("/login/user")
  public ResponseEntity<?> login(@RequestBody LoginInput loginInput) {
    UserDto user = authService.authenticateUser(loginInput);

    String token = tokenProvider.generateToken(user.getEmail(),
        GrantUtils.getStringAuthoritiesByUserType(user.getUserType()));

    return ResponseEntity.ok(new AuthResponse(user.getEmail(), user.getNickname(), token));
  }

}
