package com.example.fitnessrecord.domain.userbodyinfo.controller;

import com.example.fitnessrecord.domain.userbodyinfo.dto.BodyInfoDto;
import com.example.fitnessrecord.domain.userbodyinfo.dto.BodyInfoInput;
import com.example.fitnessrecord.domain.userbodyinfo.service.BodyInfoService;
import com.example.fitnessrecord.global.auth.sercurity.principal.PrincipalDetails;
import io.swagger.annotations.ApiOperation;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BodyInfoController {

  private final BodyInfoService bodyInfoService;

  @ApiOperation("사용자 Body Info 등록")
  @PostMapping("/body-info/add")
  public ResponseEntity<?> addInfo(@RequestBody BodyInfoInput input,
      @AuthenticationPrincipal PrincipalDetails principalDetails) {
    BodyInfoDto result = bodyInfoService.addBodyInfo(principalDetails.getUserId(), input);
    return ResponseEntity.ok(result);
  }

  @ApiOperation("사용자 Body Info 변화를 기간별 검색")
  @GetMapping("/body-info/list")
  public ResponseEntity<?> bodyInfoList(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {

    return ResponseEntity.ok(null);
  }


}
