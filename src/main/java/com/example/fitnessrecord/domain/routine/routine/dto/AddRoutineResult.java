package com.example.fitnessrecord.domain.routine.routine.dto;

import com.example.fitnessrecord.domain.routine.routine.persist.Routine;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@AllArgsConstructor
@Builder
public class AddRoutineResult {
  private Long id;
  private String username;
  private String routineName;
  private LocalDateTime lastModifiedDate;

  public static AddRoutineResult fromEntity(Routine routine){
    return AddRoutineResult.builder()
        .id(routine.getId())
        .username(routine.getUser().getEmail())
        .routineName(routine.getRoutineName())
        .lastModifiedDate(LocalDateTime.now())
        .build();
  }
}
