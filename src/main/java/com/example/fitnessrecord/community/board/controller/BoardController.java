package com.example.fitnessrecord.community.board.controller;

import com.example.fitnessrecord.community.board.dto.BoardMainDto;
import com.example.fitnessrecord.community.board.service.BoardService;
import com.example.fitnessrecord.community.likes.dto.LikesDto;
import com.example.fitnessrecord.community.likes.service.LikesService;
import com.example.fitnessrecord.community.routinepost.dto.RoutinePostDto;
import com.example.fitnessrecord.community.routinepost.service.RoutinePostService;
import com.example.fitnessrecord.global.auth.sercurity.principal.PrincipalDetails;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/board")
@RestController
public class BoardController {

  private final BoardService boardService;
  private final RoutinePostService routinePostService;
  private final LikesService likesService;

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

  @ApiOperation("루틴 공유 글에 좋아요(likes)를 누른다.")
  @PostMapping("/post/{id}/likes")
  public ResponseEntity<?> likesRoutinePost(@PathVariable Long id,
      @AuthenticationPrincipal PrincipalDetails principalDetails) {

    LikesDto result = likesService.doLikes(principalDetails.getUserId(), id);

    return ResponseEntity.ok(result);
  }

  @ApiOperation("루틴 공유 글에 좋아요(likes)를 취소한다.")
  @DeleteMapping("/post/{id}/likes")
  public ResponseEntity<?> cancelLikesRoutinePost(@PathVariable Long id,
      @AuthenticationPrincipal PrincipalDetails principalDetails) {

    LikesDto result = likesService.cancelLikes(principalDetails.getUserId(), id);

    return ResponseEntity.ok(result);
  }

}
