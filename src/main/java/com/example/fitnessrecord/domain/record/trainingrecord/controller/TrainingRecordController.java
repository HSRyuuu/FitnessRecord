package com.example.fitnessrecord.domain.record.trainingrecord.controller;

import com.example.fitnessrecord.domain.record.trainingrecord.dto.TrainingRecordDto;
import com.example.fitnessrecord.domain.record.trainingrecord.dto.TrainingRecordListResponse;
import com.example.fitnessrecord.domain.record.trainingrecord.dto.TrainingRecordResponse;
import com.example.fitnessrecord.domain.record.trainingrecord.service.TrainingRecordService;
import com.example.fitnessrecord.global.auth.sercurity.principal.PrincipalDetails;
import io.swagger.annotations.ApiOperation;
import java.time.LocalDate;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class TrainingRecordController {

  private final TrainingRecordService trainingRecordService;

  @ApiOperation(value = "운동을 시작할 때, 가장 큰 단위인 TrainingRecord를 하나 생성한다.",
      notes = "여기서 생성된 운동의 id로 각 SetRecord를 저장한다.")
  @PostMapping("/record/training/add")
  public ResponseEntity<?> addTrainingRecord(
      @AuthenticationPrincipal PrincipalDetails principalDetails) {
    TrainingRecordDto result = trainingRecordService.addTrainingRecord(
        principalDetails.getUserId());
    return ResponseEntity.ok(result);
  }

  @ApiOperation("운동 기록을 조회한다. 운동 ID로 조회")
  @GetMapping("/record/training/{id}")
  public ResponseEntity<?> trainingRecord(@PathVariable Long id,
      @AuthenticationPrincipal PrincipalDetails principalDetails) {
    TrainingRecordResponse result = trainingRecordService.getTrainingRecordInfo(id,
        principalDetails.getUsername());

    return ResponseEntity.ok(result);
  }

  @ApiOperation(value = "날짜 d1부터 d2 사이의 운동 기록을 조회한다.", notes = "d1만 입력할 경우 해당 날짜의 운동기록을 반환한다.")
  @GetMapping("/record/training")
  public ResponseEntity<?> trainingRecords(
      @RequestParam("d1") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
      @RequestParam(value = "d2", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end,
      @AuthenticationPrincipal PrincipalDetails principalDetails) {

    TrainingRecordListResponse result =
        trainingRecordService.getTrainingRecordList(principalDetails.getUserId(), start, end);

    return ResponseEntity.ok(result);
  }
}
