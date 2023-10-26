package com.example.fitnessrecord.domain.training.custom.dto;

import com.example.fitnessrecord.domain.training.common.type.BodyPart;
import com.example.fitnessrecord.domain.training.custom.persist.CustomTraining;
import com.example.fitnessrecord.domain.user.persist.User;
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
public class CustomTrainingDto {

  private Long id;

  private String username;

  private String trainingName;

  private BodyPart bodyPart;

  public static CustomTrainingDto fromEntity(CustomTraining customTraining){
    return CustomTrainingDto.builder()
        .id(customTraining.getId())
        .username(customTraining.getUser().getEmail())
        .trainingName(customTraining.getTrainingName())
        .bodyPart(customTraining.getBodyPart())
        .build();
  }
}
