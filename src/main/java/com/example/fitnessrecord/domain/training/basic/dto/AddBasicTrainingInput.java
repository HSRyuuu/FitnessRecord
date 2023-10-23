package com.example.fitnessrecord.domain.training.basic.dto;

import com.example.fitnessrecord.domain.training.common.type.BodyPart;
import lombok.Data;

@Data
public class AddBasicTrainingInput {
  private String trainingName;
  private String trainingNameKor;
  private BodyPart bodyPart;
}
