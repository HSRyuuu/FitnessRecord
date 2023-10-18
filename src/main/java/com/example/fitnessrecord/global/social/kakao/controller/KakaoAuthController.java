package com.example.fitnessrecord.global.social.kakao.controller;

import com.example.fitnessrecord.domain.user.service.UserService;
import com.example.fitnessrecord.global.social.kakao.api.KakaoApi;
import com.example.fitnessrecord.global.social.kakao.model.KakaoProfile;
import com.example.fitnessrecord.global.social.kakao.model.OAuthToken;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class KakaoAuthController {

    private final KakaoApi kakaoApi;

    private final UserService userService;

    @RequestMapping("/login/kakao")
    public KakaoProfile kakaoLogin(@RequestParam String code){
        // 토큰 받기
        OAuthToken oAuthToken = kakaoApi.getOAuthToken(code);

        // 사용자 정보 받기
        KakaoProfile kakaoProfile = kakaoApi.getUserInfo(oAuthToken.getAccess_token());

        boolean isExist = userService.userExistsByEmail(kakaoProfile.getEmail());

        //TODO : 카카오 유저 - 시큐리티, 회원가입, 로그인 처리
        if(!isExist){//회원가입

        }else{//로그인 처리

        }

        log.info("LOGIN : {}", kakaoProfile);
        return kakaoProfile;
    }



}
