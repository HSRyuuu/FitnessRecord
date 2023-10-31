package com.example.fitnessrecord.domain.record.setrecord.controller;


import com.example.fitnessrecord.domain.record.setrecord.dto.AddSetRecordResult;
import com.example.fitnessrecord.domain.record.setrecord.dto.SetRecordInput;
import com.example.fitnessrecord.domain.record.setrecord.persist.SetRecord;
import com.example.fitnessrecord.domain.record.setrecord.service.SetRecordService;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/record")
@Validated
public class SetRecordController {

  private final SetRecordService setRecordService;

  @ApiOperation(value = "SetRecord를 추가한다.",notes = "Training Record를 먼저 생성한 뒤 Set Record를 추가한다. ")
  @PostMapping("/set-record")
  public ResponseEntity<?> addSetRecord(@RequestParam("id") Long trainingRecordId,
      @Valid @RequestBody  List<SetRecordInput> list) {

    AddSetRecordResult result = setRecordService.addSetRecords(trainingRecordId, list);

    return ResponseEntity.ok(result);
  }
}
