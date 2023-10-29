package com.example.fitnessrecord.domain.record.trainingrecord.service;

import com.example.fitnessrecord.domain.record.setrecord.dto.SetRecordDto;
import com.example.fitnessrecord.domain.record.trainingrecord.dto.TrainingRecordDto;
import com.example.fitnessrecord.domain.record.trainingrecord.dto.TrainingRecordListResponse;
import com.example.fitnessrecord.domain.record.trainingrecord.dto.TrainingRecordResponse;
import java.time.LocalDate;
import java.util.List;

public interface TrainingRecordService {
  TrainingRecordDto addTrainingRecord(Long userId);
  TrainingRecordResponse getTrainingRecordInfo(Long trainingRecordId, String username);

  TrainingRecordListResponse getTrainingRecordList(Long userId, LocalDate start, LocalDate end);

}
