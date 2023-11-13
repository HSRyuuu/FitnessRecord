package com.example.fitnessrecord.community.board.dto;

import com.example.fitnessrecord.community.routinepost.persist.RoutinePost;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
public class BoardMainDto {

  private List<RoutinePost> hotPosts;

  private Page<RoutinePost> posts;

  public BoardMainDto(List<RoutinePost> hotPosts) {
    this.hotPosts = hotPosts;
  }

  public BoardMainDto(Page<RoutinePost> posts) {
    this.posts = posts;
  }

  public BoardMainDto(List<RoutinePost> hotPosts, Page<RoutinePost> posts) {
    this.hotPosts = hotPosts;
    this.posts = posts;
  }
}
