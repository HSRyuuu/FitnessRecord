package com.example.fitnessrecord.community.likes.dto;

import com.example.fitnessrecord.community.likes.persist.Likes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LikesDto {

  private Long id;

  private Long userId;

  private Long routinePostId;

  public static LikesDto fromEntity(Likes likes){
    return new LikesDto(likes.getId(), likes.getUserId(), likes.getRoutinePostId());
  }
}
