
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
public class RoutinePostDto {

  private Long id;

  private String nickname;

  private String title;
  private String content;

  private LocalDateTime createDateTime;
  private LocalDateTime lastModifiedDateTime;

  private int views;
  private int likes;

  private List<RoutineElementDto> elements;

  public void addView(boolean b){
    if(b){
      this.views += 1;
    }
  }

  public static RoutinePostDto fromEntity(RoutinePost routinePost,
      List<RoutineElementDto> elements) {
    return RoutinePostDto.builder()
        .id(routinePost.getId())
        .nickname(routinePost.getUser().getNickname())
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
