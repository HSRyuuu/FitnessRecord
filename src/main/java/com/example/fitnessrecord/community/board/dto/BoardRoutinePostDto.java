package com.example.fitnessrecord.community.board.dto;

import com.example.fitnessrecord.community.routinepost.persist.RoutinePost;
import java.time.LocalDateTime;

/**
 * 게시판 메인에 보여줄 RoutinePostDto
 */
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
public class BoardRoutinePostDto {
  private Long id;

  private String nickname;
  private String title;

  private LocalDateTime createDateTime;
  private LocalDateTime lastModifiedDateTime;

  private int views;
  private int likes;

  public static BoardRoutinePostDto fromEntity(RoutinePost routinePost){
    return BoardRoutinePostDto.builder()
        .id(routinePost.getId())
        .nickname(routinePost.getUser().getNickname())
        .title(routinePost.getTitle())
        .createDateTime(routinePost.getCreateDateTime())
        .lastModifiedDateTime(routinePost.getLastModifiedDateTime())
        .views(routinePost.getViews())
        .likes(routinePost.getLikes())
        .build();

  }
}
