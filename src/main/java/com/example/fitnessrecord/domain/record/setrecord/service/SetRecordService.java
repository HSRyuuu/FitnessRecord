package com.example.fitnessrecord.domain.record.setrecord.service;

import com.example.fitnessrecord.domain.record.setrecord.dto.AddSetRecordResult;
import com.example.fitnessrecord.domain.record.setrecord.dto.SetRecordInput;
import com.example.fitnessrecord.domain.record.setrecord.persist.SetRecord;
import java.util.List;

public interface SetRecordService {

  AddSetRecordResult addSetRecords(Long trainingRecordId, List<SetRecordInput> list);
}
