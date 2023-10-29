package com.example.fitnessrecord.domain.record.trainingrecord.service;

import com.example.fitnessrecord.domain.record.setrecord.dto.SetRecordDto;
import com.example.fitnessrecord.domain.record.trainingrecord.dto.TrainingRecordDto;
import java.util.List;

public interface TrainingRecordService {
  TrainingRecordDto addTrainingRecord(Long userId);
  TrainingRecordDto getTrainingRecord(Long trainingRecordId);
  List<SetRecordDto> getSetRecordList(Long trainingRecordId, String username);

}
