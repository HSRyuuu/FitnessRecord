package com.example.fitnessrecord.domain.record.trainingrecord.service;

import com.example.fitnessrecord.domain.record.trainingrecord.dto.TrainingRecordDto;

public interface TrainingRecordService {
  TrainingRecordDto addRecord(Long userId);

}
