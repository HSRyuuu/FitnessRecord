package com.example.fitnessrecord.domain.social.common.model;

import com.example.fitnessrecord.domain.social.common.component.AuthConstUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
