package com.example.fitnessrecord.domain.record.trainingrecord.controller;

import com.example.fitnessrecord.domain.record.setrecord.dto.SetRecordDto;
import com.example.fitnessrecord.domain.record.trainingrecord.dto.SetRecordListResponse;
import com.example.fitnessrecord.domain.record.trainingrecord.dto.TrainingRecordDto;
import com.example.fitnessrecord.domain.record.trainingrecord.service.TrainingRecordService;
import com.example.fitnessrecord.global.auth.sercurity.principal.PrincipalDetails;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
  public ResponseEntity<?> addTrainingRecord(
      @AuthenticationPrincipal PrincipalDetails principalDetails) {
    TrainingRecordDto result = trainingRecordService.addTrainingRecord(principalDetails.getUserId());
    return ResponseEntity.ok(result);
  }

  @ApiOperation("운동 기록을 조회한다.")
  @GetMapping("/record/training/{id}")
  public ResponseEntity<?> setRecordList(@PathVariable Long id,
      @AuthenticationPrincipal PrincipalDetails principalDetails) {
    List<SetRecordDto> setRecordList = trainingRecordService.getSetRecordList(id,
        principalDetails.getUsername());

    TrainingRecordDto trainingRecord = trainingRecordService.getTrainingRecord(id);

    return ResponseEntity.ok(new SetRecordListResponse(trainingRecord, setRecordList));
  }

}
