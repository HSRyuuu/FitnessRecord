package com.example.fitnessrecord.community.routinepost.controller;

import com.example.fitnessrecord.community.likes.dto.LikesDto;
import com.example.fitnessrecord.community.likes.service.LikesService;
import com.example.fitnessrecord.community.routinepost.dto.RoutinePostDto;
import com.example.fitnessrecord.community.routinepost.service.RoutinePostService;
import com.example.fitnessrecord.domain.routine.routine.dto.RoutineDto;
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
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/board")
@RestController
public class UserRoutinePostController {

  private final RoutinePostService routinePostService;
  private final LikesService likesService;

  @ApiOperation("루틴 공유 글을 조회한다.")
  @GetMapping("/post/{id}")
  public ResponseEntity<?> getRoutinePost(@PathVariable Long id,
      @AuthenticationPrincipal PrincipalDetails principalDetails) {
    RoutinePostDto result = routinePostService.getRoutinePost(id, principalDetails.getUserId());

    boolean addViewResult = routinePostService.addView(principalDetails.getUserId(),
        result.getId());

    result.addView(addViewResult);
    log.info("조회수 증가 성공 여부: {}", addViewResult);
    return ResponseEntity.ok(result);
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

  @ApiOperation("다른 유저의 루틴을 저장한다.")
  @PostMapping("/post/{id}/save")
  public ResponseEntity<?> quoteAndSaveRoutine(@PathVariable("id") Long routinePostId,
      @AuthenticationPrincipal PrincipalDetails principalDetails) {
    RoutineDto result = routinePostService.quoteAndSaveRoutine(routinePostId,
        principalDetails.getUserId());

    return ResponseEntity.ok(result);
  }

}
