package com.example.fitnessrecord.community.routinepost.dto;

import com.example.fitnessrecord.community.routinepost.persist.RoutinePost;
import com.example.fitnessrecord.domain.routine.element.dto.RoutineElementDto;
import java.time.LocalDateTime;

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
public class RoutinePostResult {

  private Long id;

  private Long userId;
  private Long routineId;

  private String title;
  private String content;

  private LocalDateTime createDateTime;
  private LocalDateTime lastModifiedDateTime;

  private int views;
  private int likes;

  private List<RoutineElementDto> elements;

  public static RoutinePostResult fromEntity(RoutinePost routinePost, List<RoutineElementDto> elements){
    return RoutinePostResult.builder()
        .id(routinePost.getId())
        .userId(routinePost.getUser().getId())
        .routineId(routinePost.getRoutine().getId())
        .title(routinePost.getTitle())
        .content(routinePost.getContent())
        .createDateTime(routinePost.getCreateDateTime())
        .lastModifiedDateTime(routinePost.getLastModifiedDateTime())
        .views(routinePost.getViews())
        .likes(routinePost.getLikes())
        .elements(elements)
        .build();
  }

}
