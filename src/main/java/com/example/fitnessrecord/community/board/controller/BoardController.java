package com.example.fitnessrecord.community.board.controller;

import com.example.fitnessrecord.community.board.dto.BoardMainDto;
import com.example.fitnessrecord.community.board.service.BoardService;
import com.example.fitnessrecord.community.routinepost.dto.RoutinePostDto;
import com.example.fitnessrecord.community.routinepost.service.RoutinePostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController("/board")
public class BoardController {

  private final BoardService boardService;
  private final RoutinePostService routinePostService;

  @ApiOperation("루틴 공유 게시판 Main 화면")
  @GetMapping("/main")
  public ResponseEntity<?> board(@RequestParam(value = "p", defaultValue = "1") Integer page) {
    BoardMainDto boardMainData = boardService.getBoardMainData(page);
    return ResponseEntity.ok(boardMainData);
  }

  @ApiOperation("루틴 공유 글을 조회한다.")
  @GetMapping("/post/{id}")
  public ResponseEntity<?> getRoutinePost(@PathVariable Long id) {
    RoutinePostDto routinePost = routinePostService.getRoutinePost(id);
    return ResponseEntity.ok(routinePost);
  }

}
