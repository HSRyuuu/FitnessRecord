package com.example.fitnessrecord.domain.training.basic.dto;

import com.example.fitnessrecord.domain.training.common.type.BodyPart;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EditBasicTrainingInput {
  private Long id;
  private String trainingName;
  private String trainingNameKor;
  private BodyPart bodyPart;
}
