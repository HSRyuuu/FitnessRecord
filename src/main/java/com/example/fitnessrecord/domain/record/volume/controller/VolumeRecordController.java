package com.example.fitnessrecord.domain.record.volume.controller;

import com.example.fitnessrecord.domain.record.trainingrecord.service.TrainingRecordService;
import com.example.fitnessrecord.domain.record.volume.service.VolumeRecordService;
import com.example.fitnessrecord.global.auth.sercurity.principal.PrincipalDetails;
import com.example.fitnessrecord.global.exception.ErrorCode;
import com.example.fitnessrecord.global.exception.MyException;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class VolumeRecordController {

  private final TrainingRecordService trainingRecordService;
  private final VolumeRecordService volumeRecordService;

  @ApiOperation(value = "TrainingRecord, SetRecord 변경 시 저장 완료 버튼 같은 저장 처리를 해줘야한다.",
      notes = "VolumeRecord를 수정해주기 위함")
  @PostMapping("/training-records/{id}/update-volume")
  public ResponseEntity<?> updateVolume(@PathVariable Long id,
      @AuthenticationPrincipal PrincipalDetails principalDetails) {
    boolean hasAuthority = trainingRecordService.hasAuthority(principalDetails.getUserId(), id);

    if (!hasAuthority) {
      throw new MyException(ErrorCode.NO_AUTHORITY_ERROR);
    }

    boolean result = volumeRecordService.updateVolumeRecord(id);

    return ResponseEntity.ok(result);
  }

}
