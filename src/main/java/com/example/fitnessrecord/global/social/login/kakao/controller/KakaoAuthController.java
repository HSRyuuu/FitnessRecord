package com.example.fitnessrecord.global.social.login.kakao.controller;

import com.example.fitnessrecord.domain.user.dto.UserDto;
import com.example.fitnessrecord.domain.user.persist.User;
import com.example.fitnessrecord.domain.user.service.UserService;
import com.example.fitnessrecord.global.auth.dto.TokenResponse;
import com.example.fitnessrecord.global.auth.sercurity.jwt.JwtTokenService;
import com.example.fitnessrecord.global.social.login.kakao.api.KakaoApi;
import com.example.fitnessrecord.global.social.login.kakao.model.KakaoProfile;
import com.example.fitnessrecord.global.social.login.kakao.model.OAuthToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class KakaoAuthController {

  private final KakaoApi kakaoApi;

  private final UserService userService;
  private final JwtTokenService jwtTokenService;

  @RequestMapping("/login/kakao")
  public ResponseEntity<?> kakaoLogin(@RequestParam String code) {
    // 토큰 받기
    OAuthToken oAuthToken = kakaoApi.getOAuthToken(code);

    // 사용자 정보 받기
    KakaoProfile kakaoProfile = kakaoApi.getUserInfo(oAuthToken.getAccess_token());

    //유저 존재 확인
    boolean isExist = userService.userExistsByEmail(kakaoProfile.getEmail());

    UserDto userDto;
    if (!isExist) {//회원가입
      User user = KakaoProfile.toEntity(kakaoProfile);
      userDto = userService.registerSocialUser(user);
    } else {
      userDto = userService.findByEmail(kakaoProfile.getEmail());
    }

    TokenResponse tokenResponse =
        jwtTokenService.generateTokenResponse(userDto.getEmail(), userDto.getUserType());

    return ResponseEntity.ok(tokenResponse);
  }
}
