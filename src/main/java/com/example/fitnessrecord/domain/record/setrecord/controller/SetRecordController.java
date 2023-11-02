package com.example.fitnessrecord.domain.record.setrecord.controller;


import com.example.fitnessrecord.domain.record.setrecord.dto.AddSetRecordResult;
import com.example.fitnessrecord.domain.record.setrecord.dto.DeleteSetRecordResult;
import com.example.fitnessrecord.domain.record.setrecord.dto.SetRecordDto;
import com.example.fitnessrecord.domain.record.setrecord.dto.SetRecordInput;
import com.example.fitnessrecord.domain.record.setrecord.service.SetRecordService;
import com.example.fitnessrecord.domain.record.trainingrecord.service.TrainingRecordService;
import com.example.fitnessrecord.global.auth.sercurity.principal.PrincipalDetails;
import com.example.fitnessrecord.global.exception.ErrorCode;
import com.example.fitnessrecord.global.exception.MyException;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/record")
@Validated
public class SetRecordController {

  private final SetRecordService setRecordService;
  private final TrainingRecordService trainingRecordService;

  @ApiOperation(value = "SetRecord를 여러개 추가한다.", notes = "Training Record를 먼저 생성한 뒤 Set Record를 추가한다. ")
  @PostMapping("/training-record/{id}/set-records")
  public ResponseEntity<?> addSetRecords(@PathVariable("id") Long trainingRecordId,
      @Valid @RequestBody List<SetRecordInput> list) {

    AddSetRecordResult result = setRecordService.addSetRecords(trainingRecordId, list);

    return ResponseEntity.ok(result);
  }

  @ApiOperation("SetRecord를 하나 추가한다.")
  @PostMapping("/training-record/{id}/set-record")
  public ResponseEntity<?> addSetRecord(@PathVariable("id") Long trainingRecordId,
      @Valid @RequestBody SetRecordInput input,
      @AuthenticationPrincipal PrincipalDetails principalDetails) {
    if (!trainingRecordService.hasAuthority(principalDetails.getUserId(), trainingRecordId)) {
      throw new MyException(ErrorCode.NO_AUTHORITY_ERROR);
    }
    SetRecordDto setRecordDto = setRecordService.addSetRecord(trainingRecordId, input);

    return ResponseEntity.ok(setRecordDto);
  }

  @ApiOperation("set-record 삭제")
  @DeleteMapping("/set-record/{id}")
  public ResponseEntity<?> deleteSetRecord(@PathVariable Long id,
      @AuthenticationPrincipal PrincipalDetails principalDetails) {
    DeleteSetRecordResult result = setRecordService.deleteSetRecord(id,
        principalDetails.getUserId());

    return ResponseEntity.ok(result);
  }

  @ApiOperation("set-record 수정")
  @PutMapping("/set-record/{id}")
  public ResponseEntity<?> updateSetRecord(@PathVariable Long id,
      @RequestBody SetRecordInput input,
      @AuthenticationPrincipal PrincipalDetails principalDetails){

    /*
    * delete와 다르게 hadAuthority로 권한 확인을 분리해 봤습니다.
    * 이렇게하면 hasAuthority에서 setRecord와 user를 찾는 쿼리 두번이 추가로 나가게 되지만,
    * 단일 책임 원칙에 조금 더 가까운 것 같습니다.
    *
    * 쿼리가 조금 더 나가더라도 이 방식이 나을지 deleteSetRecord처럼 하나의 서비스 메서드에서
    * 권한 확인과 서비스 로직을 동시에 해서 권한확인 할 때 썼던 setRecord를 사용할 수 있는 이점을 챙기는게 나을까요?
    * 아니면 아래처럼 이렇게 분리하는게 나을까요?
    * */
      if(!setRecordService.hasAuthority(id, principalDetails.getUserId())){
        throw new MyException(ErrorCode.NO_AUTHORITY_ERROR);
      }

    SetRecordDto result = setRecordService.updateSetRecord(id, input);

    return ResponseEntity.ok(result);
  }


}
