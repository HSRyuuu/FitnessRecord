package com.example.fitnessrecord.domain.training.custom.controller;

import com.example.fitnessrecord.domain.training.custom.dto.AddCustomTrainingInput;
import com.example.fitnessrecord.domain.training.custom.dto.CustomTrainingDto;
import com.example.fitnessrecord.domain.training.custom.dto.EditCustomTrainingInput;
import com.example.fitnessrecord.domain.training.custom.service.CustomTrainingService;
import com.example.fitnessrecord.global.auth.sercurity.principal.PrincipalDetails;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CustomTrainingController {

  private final CustomTrainingService customTrainingService;
  @ApiOperation("CustomTraining 추가")
  @PostMapping("/user/custom-training/add")
  public ResponseEntity<?> addCustomTraining(@RequestBody AddCustomTrainingInput input,
      @AuthenticationPrincipal PrincipalDetails principalDetails) {

    CustomTrainingDto result =
        customTrainingService.addCustomTraining(principalDetails.getEmail(), input);

    return ResponseEntity.ok(result);
  }
  @ApiOperation("CustomTraining 수정")
  @PutMapping("/user/custom-training/edit")
  public ResponseEntity<?> editCustomTraining(@RequestBody EditCustomTrainingInput input,
      @AuthenticationPrincipal PrincipalDetails principalDetails) {

    CustomTrainingDto result =
        customTrainingService.editCustomTraining(principalDetails.getEmail(), input);

    return ResponseEntity.ok(result);
  }
  @ApiOperation("CustomTraining 삭제")
  @DeleteMapping("/user/custom-training/delete")
  public ResponseEntity<?> deleteCustomTraining(@RequestParam Long id,
        @AuthenticationPrincipal PrincipalDetails principalDetails){

    CustomTrainingDto result = customTrainingService.deleteCustomTraining(
        principalDetails.getEmail(), id);

    return ResponseEntity.ok(result);
  }

  @ApiOperation(value = "CustomTraining 리스트", notes = "로그인 된 유저를 바탕으로 커스텀 운동 정보를 반환")
  @GetMapping("/user/custom-training/list")
  public ResponseEntity<?> customTrainingList(@RequestParam(required = false, defaultValue = "1") Integer page,
      @AuthenticationPrincipal PrincipalDetails principalDetails){

    Page<CustomTrainingDto> customTrainingList =
        customTrainingService.customTrainingList(principalDetails.getEmail(), page);

    return ResponseEntity.ok(customTrainingList);
  }

}
