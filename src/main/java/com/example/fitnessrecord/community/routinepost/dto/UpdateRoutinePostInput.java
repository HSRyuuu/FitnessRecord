package com.example.fitnessrecord.community.routinepost.dto;

import com.example.fitnessrecord.community.routinepost.persist.RoutinePost;
import com.example.fitnessrecord.domain.routine.routine.persist.Routine;
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
public class UpdateRoutinePostInput {

  private String title;
  private String content;

}
