package com.example.fitnessrecord.domain.userbodyinfo.controller;

import com.example.fitnessrecord.domain.userbodyinfo.dto.BodyInfoDto;
import com.example.fitnessrecord.domain.userbodyinfo.dto.BodyInfoInput;
import com.example.fitnessrecord.domain.userbodyinfo.dto.BodyInfoListResult;
import com.example.fitnessrecord.domain.userbodyinfo.service.BodyInfoService;
import com.example.fitnessrecord.global.auth.sercurity.principal.PrincipalDetails;
import io.swagger.annotations.ApiOperation;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
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
  @PostMapping("/body-info")
  public ResponseEntity<?> addBodyInfo(@RequestBody BodyInfoInput input,
      @AuthenticationPrincipal PrincipalDetails principalDetails) {
    BodyInfoDto result = bodyInfoService.addBodyInfo(principalDetails.getUserId(), input);
    return ResponseEntity.ok(result);
  }

  @ApiOperation("사용자 Body Info 삭제")
  @DeleteMapping("/body-info")
  public ResponseEntity<?> deleteBodyInfo(
      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
      @AuthenticationPrincipal PrincipalDetails principalDetails) {
    BodyInfoDto deletedBodyInfo = bodyInfoService.deleteByDate(principalDetails.getUserId(), date);

    return ResponseEntity.ok(deletedBodyInfo);
  }

  @ApiOperation("사용자 Body Info 변화를 기간별 검색")
  @GetMapping("/body-infos")
  public ResponseEntity<?> bodyInfoList(
      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end,
      @AuthenticationPrincipal PrincipalDetails principalDetails) {

    List<BodyInfoDto> findList =
        bodyInfoService.bodyInfoListByPeriod(principalDetails.getUserId(), start, end);

    return ResponseEntity.ok(BodyInfoListResult.builder()
        .userId(principalDetails.getUserId())
        .userEmail(principalDetails.getEmail())
        .start(start)
        .end(end)
        .list(findList).build());
  }


}
