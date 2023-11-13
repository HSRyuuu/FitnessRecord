package com.example.fitnessrecord.domain.routine.element.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteRoutineElementDto {

  @NotNull
  private Long routineId; //루틴 ID

  @NotNull
  private Long routineElementId;//루틴 요소 ID
}
