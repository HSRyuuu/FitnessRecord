package com.example.fitnessrecord.domain.routine.element.dto;

import com.example.fitnessrecord.domain.routine.element.persist.RoutineElement;
import com.example.fitnessrecord.domain.routine.routine.persist.Routine;
import com.example.fitnessrecord.domain.training.common.type.BodyPart;
import javax.validation.constraints.NotNull;
import lombok.Data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddRoutineElementInput {

  private Long routineId; //루틴 ID

  @NotNull(message = "순서를 입력해주세요.")
  @Range(min = 1L, max = 100L, message = "순서는 1부터 100까지의 숫자만 입력 가능합니다.")
  private int orderNumber; //순서

  private String trainingName; //운동 명

  private BodyPart bodyPart; //운동 부위

  private Integer reps; //횟수

  public static RoutineElement toEntity(AddRoutineElementInput input, Routine routine){
    return RoutineElement.builder()
        .routine(routine)
        .orderNumber(input.getOrderNumber())
        .trainingName(input.getTrainingName())
        .bodyPart(input.getBodyPart())
        .reps(input.getReps())
        .build();
  }
}
