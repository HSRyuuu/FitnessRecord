package com.example.fitnessrecord.community.routinecomment.service;

import com.example.fitnessrecord.community.routinecomment.dto.RoutineCommentDto;

public interface RoutineCommentService {

  /**
   * 댓글 작성
   */
  RoutineCommentDto addRoutineComment(Long postId, Long userId, String text);

  /**
   * 댓글 삭제
   */
  RoutineCommentDto deleteRoutineComment(Long commentId, Long userId);

  /**
   * 댓글 수정
   */
  RoutineCommentDto updateRoutineComment(Long commentId, Long userId, String text);
}
