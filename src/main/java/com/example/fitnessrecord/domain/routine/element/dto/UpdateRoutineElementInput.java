package com.example.fitnessrecord.domain.routine.element.dto;

import com.example.fitnessrecord.domain.routine.element.persist.RoutineElement;
import com.example.fitnessrecord.domain.routine.routine.persist.Routine;
import com.example.fitnessrecord.domain.training.common.type.BodyPart;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateRoutineElementInput {

  private String trainingName; //운동 명

  private BodyPart bodyPart; //운동 부위

  private Integer reps; //횟수

}
