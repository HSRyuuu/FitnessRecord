package com.example.fitnessrecord.domain.routine.routine.dto;

import com.example.fitnessrecord.domain.routine.element.dto.RoutineElementDto;
import com.example.fitnessrecord.domain.routine.element.persist.RoutineElement;
import com.example.fitnessrecord.domain.user.dto.UserDto;
import com.example.fitnessrecord.domain.user.persist.User;
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
  private UserDto user;

  private String routineName;

  private LocalDateTime lastModifiedDateTime;

  private String description;

  private List<RoutineElementDto> elementList = new ArrayList<>();


}
