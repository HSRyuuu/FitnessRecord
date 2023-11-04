package com.example.fitnessrecord.domain.routine.element.dto;

import com.example.fitnessrecord.domain.routine.element.persist.RoutineElement;
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
public class RoutineElementDto {

  private Long id;

  private Long routineId;

  private int orderNumber; //순서

  private String trainingName; //운동 명

  private BodyPart bodyPart; //운동 부위

  private Integer reps; //횟수

  public static RoutineElementDto fromEntity(RoutineElement routineElement){
    return RoutineElementDto.builder()
        .id(routineElement.getId())
        .routineId(routineElement.getRoutine().getId())
        .orderNumber(routineElement.getOrderNumber())
        .trainingName(routineElement.getTrainingName())
        .bodyPart(routineElement.getBodyPart())
        .reps(routineElement.getReps())
        .build();
  }
}
