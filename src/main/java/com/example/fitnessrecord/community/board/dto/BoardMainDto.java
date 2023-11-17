package com.example.fitnessrecord.community.board.dto;

import com.example.fitnessrecord.community.routinepost.persist.RoutinePost;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
public class BoardMainDto {

  private int page;

  private List<BoardRoutinePostDto> hotPosts;

  private List<BoardRoutinePostDto> posts;

  public BoardMainDto(int page, List<RoutinePost> hotPosts, Page<RoutinePost> posts) {
    this.page = page;
    this.hotPosts = hotPosts.stream()
        .map(BoardRoutinePostDto::fromEntity)
        .collect(Collectors.toList());
    this.posts = posts.getContent().stream()
        .map(BoardRoutinePostDto::fromEntity)
        .collect(Collectors.toList());
  }
}
