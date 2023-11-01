package com.example.fitnessrecord.domain.record.trainingrecord.dto;


import com.example.fitnessrecord.domain.record.trainingrecord.persist.TrainingRecord;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainingRecordDto {

  private Long id;

  private String username;

  private LocalDate date; //운동 날짜

  private LocalDate lastModifiedDate;

  public static TrainingRecordDto fromEntity(TrainingRecord trainingRecord) {
    return TrainingRecordDto.builder()
        .id(trainingRecord.getId())
        .username(trainingRecord.getUser().getEmail())
        .date(trainingRecord.getDate())
        .lastModifiedDate(trainingRecord.getLastModifiedDate())
        .build();
  }
}
