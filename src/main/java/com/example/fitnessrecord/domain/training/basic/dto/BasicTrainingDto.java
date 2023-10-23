package com.example.fitnessrecord.domain.training.basic.dto;

import com.example.fitnessrecord.domain.training.basic.persist.BasicTraining;
import com.example.fitnessrecord.domain.training.common.type.BodyPart;
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
public class BasicTrainingDto {

  private Long id;

  private String trainingName;
  private String trainingNameKor;

  private BodyPart bodyPart;

  public static BasicTrainingDto fromEntity(BasicTraining basicTraining) {
    return BasicTrainingDto.builder()
        .id(basicTraining.getId())
        .trainingName(basicTraining.getTrainingName())
        .trainingNameKor(basicTraining.getTrainingNameKor())
        .bodyPart(basicTraining.getBodyPart())
        .build();
  }
}