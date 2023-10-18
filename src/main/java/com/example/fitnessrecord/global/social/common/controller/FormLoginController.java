package com.example.fitnessrecord.global.social.common.controller;

import com.example.fitnessrecord.global.social.common.model.AuthAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FormLoginController {

  @GetMapping("/login/social")
  public String socialLogin(Model model){
    model.addAttribute("authObject", new AuthAttributes());

    return "login/socialLoginPage";
  }

}
