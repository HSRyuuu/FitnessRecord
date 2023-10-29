package com.example.fitnessrecord.domain.record.trainingrecord.dto;


import com.example.fitnessrecord.domain.record.trainingrecord.persist.TrainingRecord;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainingRecordDto {

  private Long id;

  private String username;

  private LocalDate date; //운동 날짜

  public static TrainingRecordDto fromEntity(TrainingRecord saved) {
    return TrainingRecordDto.builder()
        .id(saved.getId())
        .username(saved.getUser().getEmail())
        .date(saved.getDate())
        .build();
  }
}
