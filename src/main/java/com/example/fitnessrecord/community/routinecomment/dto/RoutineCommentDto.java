package com.example.fitnessrecord.community.routinecomment.dto;

import com.example.fitnessrecord.community.routinecomment.persist.RoutineComment;
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
public class RoutineCommentDto {

  private Long id;

  private Long routinePostId;

  private Long writerId;

  private String text;

  private LocalDateTime createDateTime;
  private LocalDateTime lastModifiedDateTime;

  public static RoutineCommentDto fromEntity(RoutineComment routineComment){
    return RoutineCommentDto.builder()
        .id(routineComment.getId())
        .routinePostId(routineComment.getRoutinePost().getId())
        .writerId(routineComment.getWriter().getId())
        .text(routineComment.getText())
        .createDateTime(routineComment.getCreateDateTime())
        .lastModifiedDateTime(routineComment.getLastModifiedDateTime())
        .build();
  }
}
