package com.example.fitnessrecord.community.routinepost.dto;

import com.example.fitnessrecord.domain.routine.element.dto.RoutineElementDto;
import com.example.fitnessrecord.domain.routine.routine.persist.Routine;
import com.example.fitnessrecord.domain.user.dto.UserDto;
import com.example.fitnessrecord.domain.user.persist.User;
import java.time.LocalDateTime;

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
public class RoutinePostDto {

  private Long id;

  private UserDto user;

  private Routine routine;

  private String title;
  private String content;

  private LocalDateTime createDateTime;
  private LocalDateTime lastModifiedDateTime;

  private int views;
  private int likes;

}
