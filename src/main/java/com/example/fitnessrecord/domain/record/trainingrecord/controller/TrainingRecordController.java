package com.example.fitnessrecord.domain.record.trainingrecord.controller;

import com.example.fitnessrecord.domain.record.trainingrecord.dto.TrainingRecordDto;
import com.example.fitnessrecord.domain.record.trainingrecord.service.TrainingRecordService;
import com.example.fitnessrecord.global.auth.sercurity.principal.PrincipalDetails;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class TrainingRecordController {

  private final TrainingRecordService trainingRecordService;

  @ApiOperation(value = "운동을 시작할 때, 가장 큰 단위인 TrainingRecord를 하나 생성한다.",
      notes = "여기서 생성된 운동의 id로 각 SetRecord를 저장한다.")
  @PostMapping("/record/training/add")
  public ResponseEntity<?> addTrainingRecord(@AuthenticationPrincipal PrincipalDetails principalDetails){
    TrainingRecordDto result = trainingRecordService.addRecord(principalDetails.getUserId());
    return ResponseEntity.ok(result);
  }

}
