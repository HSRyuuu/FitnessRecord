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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AdminTrainingController {

  private final AdminTrainingService adminTrainingService;

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ApiOperation("ADMIN - BasicTraining 추가")
  @PostMapping("/admin/add/training")
  public ResponseEntity<?> addTraining(@RequestBody AddBasicTrainingInput input){
    BasicTrainingDto addedTraining = adminTrainingService.addTraining(input);
    return ResponseEntity.ok(addedTraining);
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ApiOperation("ADMIN - BasicTraining 삭제")
  @DeleteMapping("/admin/delete/training")
  public ResponseEntity<?> deleteTraining(@RequestParam String name){
    BasicTrainingDto deletedTraining = adminTrainingService.deleteTraining(name);
    return ResponseEntity.ok(deletedTraining);
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ApiOperation("ADMIN - BasicTraining 수정")
  @PutMapping("/admin/update/training")
  public ResponseEntity<?> updateTraining(@RequestBody EditBasicTrainingInput input){
    BasicTrainingDto updatedTraining = adminTrainingService.updateTraining(input);
    return ResponseEntity.ok(updatedTraining);
  }
  
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ApiOperation("ADMIN - BasicTraining 리스트")
  @GetMapping("/admin/list/training")
  public ResponseEntity<?> list(){
    List<BasicTrainingDto> list = adminTrainingService.list();
    return ResponseEntity.ok(list);
  }

}
