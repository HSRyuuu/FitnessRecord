package com.example.fitnessrecord.domain.record.setrecord.dto;

import com.example.fitnessrecord.domain.record.trainingrecord.dto.TrainingRecordDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddSetRecordResult {

  private TrainingRecordDto trainingRecord;
  private int addAmount;

}
