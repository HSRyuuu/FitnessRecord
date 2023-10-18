package com.example.fitnessrecord.global.social.common.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuthConstUtil {

    public static String KAKAO_API_KEY;
    public static String KAKAO_REDIRECT_URL;

    //setter 주입
    @Value("${kakao.api_key}")
    public void setKakaoApiKey(String value){
        KAKAO_API_KEY = value;
    }

    @Value("${kakao.redirect_uri}")
    public void setKakaoRedirectUrl(String value) {
        KAKAO_REDIRECT_URL = value;
    }
}
