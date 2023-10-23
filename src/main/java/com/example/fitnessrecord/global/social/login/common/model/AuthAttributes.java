package com.example.fitnessrecord.global.social.login.common.model;

import com.example.fitnessrecord.global.social.login.common.component.AuthConstUtil;
import lombok.Data;


@Data
public class AuthAttributes {

  private String kakaoApiKey;
  private String kakaoRedirectUrl;

  public AuthAttributes(){
    this.kakaoApiKey = AuthConstUtil.KAKAO_API_KEY;
    this.kakaoRedirectUrl = AuthConstUtil.KAKAO_REDIRECT_URL;
  }

}
