package com.example.fitnessrecord.domain.training.basic.dto;

import com.example.fitnessrecord.domain.training.common.type.BodyPart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AddBasicTrainingInput {
  private String trainingName;
  private String trainingNameKor;
  private BodyPart bodyPart;
}