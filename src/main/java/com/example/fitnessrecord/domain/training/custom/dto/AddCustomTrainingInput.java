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
public class AddCustomTrainingInput {

  private String trainingName;

  private BodyPart bodyPart;

  public static CustomTraining toEntity(User user, AddCustomTrainingInput input){
    return CustomTraining.builder()
        .user(user)
        .trainingName(input.getTrainingName())
        .bodyPart(input.getBodyPart())
        .build();
  }
}
