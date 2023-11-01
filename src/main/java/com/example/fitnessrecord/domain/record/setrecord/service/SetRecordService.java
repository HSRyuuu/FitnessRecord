package com.example.fitnessrecord.domain.record.setrecord.service;

import com.example.fitnessrecord.domain.record.setrecord.dto.AddSetRecordResult;
import com.example.fitnessrecord.domain.record.setrecord.dto.DeleteSetRecordResult;
import com.example.fitnessrecord.domain.record.setrecord.dto.SetRecordDto;
import com.example.fitnessrecord.domain.record.setrecord.dto.SetRecordInput;
import com.example.fitnessrecord.domain.record.setrecord.persist.SetRecord;
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
}
