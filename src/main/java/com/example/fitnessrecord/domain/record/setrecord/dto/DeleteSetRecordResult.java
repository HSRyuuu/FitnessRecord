package com.example.fitnessrecord.domain.record.setrecord.dto;

import com.example.fitnessrecord.domain.record.setrecord.persist.SetRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class DeleteSetRecordResult {

  private Long trainingRecordId;
  private String trainingName;
  private int reps;
  private double weight;

  public static DeleteSetRecordResult fromEntity(SetRecord setRecord) {
    return DeleteSetRecordResult.builder()
        .trainingRecordId(setRecord.getTrainingRecord().getId())
        .trainingName(setRecord.getTrainingName())
        .reps(setRecord.getReps())
        .weight(setRecord.getWeight())
        .build();
  }
}
