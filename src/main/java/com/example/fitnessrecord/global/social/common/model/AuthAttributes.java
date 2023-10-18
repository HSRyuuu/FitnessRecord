package com.example.fitnessrecord.global.social.common.model;

import com.example.fitnessrecord.global.social.common.component.AuthConstUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@Builder
public class AuthAttributes {

  private String kakaoApiKey;
  private String kakaoRedirectUrl;

  public AuthAttributes(){
    this.kakaoApiKey = AuthConstUtil.KAKAO_API_KEY;
    this.kakaoRedirectUrl = AuthConstUtil.KAKAO_REDIRECT_URL;
  }

}
