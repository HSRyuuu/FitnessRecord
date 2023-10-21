package com.example.fitnessrecord.domain.user.controller;

import com.example.fitnessrecord.domain.user.dto.EmailAuthResult;
import com.example.fitnessrecord.domain.user.dto.RegisterUserInput;
import com.example.fitnessrecord.domain.user.dto.RegisterUserResult;
import com.example.fitnessrecord.domain.user.dto.UserDto;
import com.example.fitnessrecord.domain.user.service.UserService;
import com.example.fitnessrecord.global.mail.MailComponents;
import com.example.fitnessrecord.global.mail.SendMailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

  private final UserService userService;
  private final MailComponents mailComponents;

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody RegisterUserInput input) {
    UserDto registeredUser = userService.register(input);

    mailComponents.sendMailForRegister(
        new SendMailDto(
            registeredUser.getEmail(),
            registeredUser.getNickname(),
            registeredUser.getEmailAuthKey())
    );

    return ResponseEntity.ok(RegisterUserResult.fromDto(registeredUser));
  }

  @GetMapping("/auth/email-auth")
  public @ResponseBody EmailAuthResult emailAuth(@RequestParam String key){
    EmailAuthResult result = userService.emailAuth(key);
    return result;
  }







}
