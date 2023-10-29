package com.example.fitnessrecord.domain.record.trainingrecord.dto;

import com.example.fitnessrecord.domain.record.setrecord.dto.SetRecordDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingRecordResponse {
  private TrainingRecordDto trainingRecord;
  private List<SetRecordDto> setList;
}
