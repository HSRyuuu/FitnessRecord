package com.example.fitnessrecord.community.routinepost.dto;

import com.example.fitnessrecord.community.routinepost.persist.RoutinePost;
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
public class DeleteRoutinePostResult {
  private Long id;
  private String writerNickname;
  private String routineName;
  private String title;

  public static DeleteRoutinePostResult fromEntity(RoutinePost routinePost){
    return DeleteRoutinePostResult.builder()
        .id(routinePost.getId())
        .writerNickname(routinePost.getUser().getNickname())
        .routineName(routinePost.getRoutine().getRoutineName())
        .title(routinePost.getTitle())
        .build();
  }
}
