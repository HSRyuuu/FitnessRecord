package com.example.fitnessrecord.domain.statistics.controller;


import com.example.fitnessrecord.global.auth.sercurity.principal.PrincipalDetails;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/statistics")
public class StatisticsController {

  @ApiOperation("d1부터 d2간의 운동 기록")
  @GetMapping("/volumes")
  public ResponseEntity<?> volumeStatistics(
      @RequestParam(required = false)@DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate d1,
      @RequestParam(required = false)@DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate d2,
      @AuthenticationPrincipal PrincipalDetails principalDetails
      ){


      return ResponseEntity.ok(null);
  }


}
