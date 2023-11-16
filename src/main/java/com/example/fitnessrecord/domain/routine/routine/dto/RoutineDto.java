package com.example.fitnessrecord.domain.routine.routine.dto;

import com.example.fitnessrecord.domain.routine.element.dto.RoutineElementDto;
import com.example.fitnessrecord.domain.routine.routine.persist.Routine;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
public class RoutineDto {

  private Long id;
  private Long userId;

  private String routineName;

  private LocalDateTime lastModifiedDateTime;

  private String description;

  private List<RoutineElementDto> elementList = new ArrayList<>();

  public static RoutineDto fromEntity(Routine routine, List<RoutineElementDto> list) {
    return RoutineDto.builder()
        .id(routine.getId())
        .userId(routine.getUser().getId())
        .routineName(routine.getRoutineName())
        .lastModifiedDateTime(routine.getLastModifiedDateTime())
        .description(routine.getDescription())
        .elementList(list)
        .build();
  }


}
