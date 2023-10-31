package com.example.fitnessrecord.domain.record.setrecord.service;

import com.example.fitnessrecord.domain.record.setrecord.dto.AddSetRecordResult;
import com.example.fitnessrecord.domain.record.setrecord.dto.SetRecordInput;
import com.example.fitnessrecord.domain.record.setrecord.persist.SetRecord;
import java.util.List;

public interface SetRecordService {

  /**
   * Training Record에 Set Record여러개를 추가한다.
   */
  AddSetRecordResult addSetRecords(Long trainingRecordId, List<SetRecordInput> list);
}
