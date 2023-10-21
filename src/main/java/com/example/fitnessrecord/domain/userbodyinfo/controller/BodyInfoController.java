package com.example.fitnessrecord.domain.userbodyinfo.controller;

import com.example.fitnessrecord.domain.userbodyinfo.dto.BodyInfoDto;
import com.example.fitnessrecord.domain.userbodyinfo.dto.BodyInfoInput;
import com.example.fitnessrecord.domain.userbodyinfo.service.BodyInfoService;
import com.example.fitnessrecord.global.auth.sercurity.principal.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BodyInfoController {

  private final BodyInfoService bodyInfoService;

  @PostMapping("/body-info/add")
  public ResponseEntity<?> addInfo(@RequestBody BodyInfoInput input,
      @AuthenticationPrincipal PrincipalDetails principalDetails) {

    BodyInfoDto result = bodyInfoService.addBodyInfo(principalDetails.getUserId(), input);
    return ResponseEntity.ok(result);
  }


}
