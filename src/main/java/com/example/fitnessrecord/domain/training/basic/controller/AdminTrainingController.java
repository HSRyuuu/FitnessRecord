package com.example.fitnessrecord.domain.training.basic.controller;

import com.example.fitnessrecord.domain.training.basic.dto.AddBasicTrainingInput;
import com.example.fitnessrecord.domain.training.basic.dto.BasicTrainingDto;
import com.example.fitnessrecord.domain.training.basic.dto.EditBasicTrainingInput;
import com.example.fitnessrecord.domain.training.basic.service.AdminTrainingService;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminTrainingController {

  private final AdminTrainingService adminTrainingService;

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ApiOperation("ADMIN - BasicTraining 추가")
  @PostMapping("/basic-training")
  public ResponseEntity<?> addTraining(@RequestBody AddBasicTrainingInput input){
    BasicTrainingDto addedTraining = adminTrainingService.addTraining(input);
    return ResponseEntity.ok(addedTraining);
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ApiOperation("ADMIN - BasicTraining 삭제")
  @DeleteMapping("/basic-training")
  public ResponseEntity<?> deleteTraining(@RequestParam String name){
    BasicTrainingDto deletedTraining = adminTrainingService.deleteTraining(name);
    return ResponseEntity.ok(deletedTraining);
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ApiOperation("ADMIN - BasicTraining 수정")
  @PutMapping("/basic-training")
  public ResponseEntity<?> updateTraining(@RequestBody EditBasicTrainingInput input){
    BasicTrainingDto updatedTraining = adminTrainingService.updateTraining(input);
    return ResponseEntity.ok(updatedTraining);
  }
  
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ApiOperation("ADMIN - BasicTraining 리스트")
  @GetMapping("/basic-trainings")
  public ResponseEntity<?> list(){
    List<BasicTrainingDto> list = adminTrainingService.trainingList();
    return ResponseEntity.ok(list);
  }

}
