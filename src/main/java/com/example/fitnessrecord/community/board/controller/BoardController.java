package com.example.fitnessrecord.community.board.controller;

import com.example.fitnessrecord.community.board.dto.BoardMainDto;
import com.example.fitnessrecord.community.board.service.BoardService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/board")
@RestController
public class BoardController {

  private final BoardService boardService;

  @ApiOperation("루틴 공유 게시판 Main 화면")
  @GetMapping("/main")
  public ResponseEntity<?> board(@RequestParam(value = "p", defaultValue = "1") Integer page) {
    BoardMainDto boardMainData = boardService.getBoardMainData(page);
    return ResponseEntity.ok(boardMainData);
  }


}
