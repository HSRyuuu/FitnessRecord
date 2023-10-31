package com.example.fitnessrecord.domain.training.custom.dto;

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
public class EditCustomTrainingInput {

  private Long id;

  private String trainingName;

  private BodyPart bodyPart;
}
