package com.example.fitnessrecord.global.social.login.common.controller;

import com.example.fitnessrecord.global.social.login.common.model.AuthAttributes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FormLoginController {

  @ApiOperation(value = "소셜 로그인을 위한 페이지", notes = "로그인 버튼을 눌렀을 때를 가정하기 위해 이부분만 view를 반환함")
  @GetMapping("/login/page")
  public String socialLogin(Model model){
    model.addAttribute("authObject", new AuthAttributes());

    return "login/socialLoginPage";
  }

}
