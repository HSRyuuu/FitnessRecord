package com.example.fitnessrecord.community.routinecomment.service;

import com.example.fitnessrecord.community.routinecomment.dto.RoutineCommentDto;
import com.example.fitnessrecord.community.routinecomment.persist.RoutineComment;
import com.example.fitnessrecord.community.routinecomment.persist.RoutineCommentRepository;
import com.example.fitnessrecord.community.routinepost.persist.RoutinePost;
import com.example.fitnessrecord.community.routinepost.persist.RoutinePostRepository;
import com.example.fitnessrecord.domain.user.persist.User;
import com.example.fitnessrecord.domain.user.persist.UserRepository;
import com.example.fitnessrecord.global.exception.ErrorCode;
import com.example.fitnessrecord.global.exception.MyException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class RoutineCommentServiceImpl implements RoutineCommentService{

  private final RoutinePostRepository routinePostRepository;
  private final RoutineCommentRepository routineCommentRepository;
  private final UserRepository userRepository;

  @Override
  public RoutineCommentDto addRoutineComment(Long postId, Long userId, String text) {
    RoutinePost routinePost = routinePostRepository.findById(postId)
        .orElseThrow(() -> new MyException(ErrorCode.ROUTINE_POST_NOT_FOUND));

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new MyException(ErrorCode.USER_NOT_FOUND));

    RoutineComment saved = routineCommentRepository.save(
        new RoutineComment(routinePost, user, text));

    return RoutineCommentDto.fromEntity(saved);
  }

  @Override
  public RoutineCommentDto deleteRoutineComment(Long postId, Long commentId, Long userId) {
    RoutineComment routineComment = routineCommentRepository.findById(commentId)
        .orElseThrow(() -> new MyException(ErrorCode.ROUTINE_COMMENT_NOT_FOUND));

    if(!routineComment.getWriter().getId().equals(userId)){
      throw new MyException(ErrorCode.NO_AUTHORITY_ERROR);
    }

    routineCommentRepository.delete(routineComment);

    return RoutineCommentDto.fromEntity(routineComment);
  }
}
