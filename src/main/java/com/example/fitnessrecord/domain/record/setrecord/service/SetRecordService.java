package com.example.fitnessrecord.domain.record.setrecord.service;

import com.example.fitnessrecord.domain.record.setrecord.dto.AddSetRecordResult;
import com.example.fitnessrecord.domain.record.setrecord.dto.DeleteSetRecordResult;
import com.example.fitnessrecord.domain.record.setrecord.dto.SetRecordDto;
import com.example.fitnessrecord.domain.record.setrecord.dto.SetRecordInput;
import java.util.List;

public interface SetRecordService {

  /**
   * TrainingRecord에 SetRecord여러개를 추가한다.
   */
  AddSetRecordResult addSetRecords(Long trainingRecordId, List<SetRecordInput> list);

  /**
   * TrainingRecord에 SetRecord를 하나 추가한다.
   */
  SetRecordDto addSetRecord(Long trainingRecordId, SetRecordInput input);

  /**
   * SetRecord를 삭제한다.
   */
  DeleteSetRecordResult deleteSetRecord(Long id, Long userId);

  /**
   * SetRecord를 수정한다.
   */
  SetRecordDto updateSetRecord(Long id, SetRecordInput input);

  /**
   * SetRecord의 쓰기 권한을 확인한다.
   */
  boolean hasAuthority(Long setRecordId, Long userId);

}
