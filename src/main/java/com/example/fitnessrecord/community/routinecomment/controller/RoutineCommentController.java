package com.example.fitnessrecord.community.routinecomment.controller;

import com.example.fitnessrecord.community.routinecomment.dto.RoutineCommentDto;
import com.example.fitnessrecord.community.routinecomment.service.RoutineCommentService;
import com.example.fitnessrecord.global.auth.sercurity.principal.PrincipalDetails;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/board")
public class RoutineCommentController {

  private final RoutineCommentService routineCommentService;

  @ApiOperation("댓글 작성")
  @PostMapping("/post/{id}/comment")
  public ResponseEntity<?> addRoutineComment(@PathVariable("id") Long postId,
      @RequestBody String text,
      @AuthenticationPrincipal PrincipalDetails principalDetails) {
    RoutineCommentDto result =
        routineCommentService.addRoutineComment(postId, principalDetails.getUserId(), text);

    return ResponseEntity.ok(result);
  }

  @ApiOperation("댓글 삭제")
  @DeleteMapping("/post/{postId}/comment/{commentId}")
  public ResponseEntity<?> deleteRoutineComment(@PathVariable Long postId,
      @PathVariable Long commentId,
      @AuthenticationPrincipal PrincipalDetails principalDetails) {

    RoutineCommentDto deleted =
        routineCommentService.deleteRoutineComment(postId, commentId, principalDetails.getUserId());

    return ResponseEntity.ok(deleted);
  }


}
