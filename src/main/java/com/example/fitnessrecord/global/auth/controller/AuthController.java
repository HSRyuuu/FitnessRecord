package com.example.fitnessrecord.global.auth.controller;

import com.example.fitnessrecord.domain.user.dto.UserDto;
import com.example.fitnessrecord.global.auth.dto.LoginInput;
import com.example.fitnessrecord.global.auth.dto.TokenResponse;
import com.example.fitnessrecord.global.auth.sercurity.jwt.JwtTokenService;
import com.example.fitnessrecord.global.auth.sercurity.principal.PrincipalDetails;
import com.example.fitnessrecord.global.auth.service.AuthService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthController {

  private final AuthService authService;
  private final JwtTokenService jwtTokenService;

  @ApiOperation(value = "로그인, JWT 토큰 발행", notes = "JWT 토큰을 Authorization 헤더에 포함시켜서 인증 처리")
  @PostMapping("/login/user")
  public ResponseEntity<?> login(@RequestBody LoginInput loginInput) {
    UserDto user = authService.authenticateUser(loginInput);

    TokenResponse tokenResponse = jwtTokenService.generateTokenResponse(user.getEmail(), user.getUserType());

    return ResponseEntity.ok(tokenResponse);
  }

  @ApiOperation("RefreshToken을 받아서 AccessToken을 새로 발행한다.")
  @PostMapping("/login/reissue")
  public ResponseEntity<?> reissueToken(@RequestHeader("Authorization") String refreshToken){
    TokenResponse tokenResponse = jwtTokenService.regenerateAccessToken(refreshToken);

    return ResponseEntity.ok(tokenResponse);
  }

  @ApiOperation("Logout: refreshToekn을 삭제한다.")
  @PostMapping("/logout/user")
  public ResponseEntity<?> logout(@AuthenticationPrincipal PrincipalDetails principalDetails,
      @RequestHeader("Authorization") String accessToken){
    log.info("Access Token : {}", accessToken);
    String token = jwtTokenService.resolveTokenFromRequest(accessToken);
    authService.logout(token, principalDetails.getEmail());
    return ResponseEntity.ok("logout complete");
  }



}
