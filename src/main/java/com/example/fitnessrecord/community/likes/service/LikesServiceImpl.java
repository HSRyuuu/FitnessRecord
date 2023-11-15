package com.example.fitnessrecord.community.likes.service;

import com.example.fitnessrecord.community.likes.dto.LikesDto;
import com.example.fitnessrecord.community.likes.persist.Likes;
import com.example.fitnessrecord.community.likes.persist.LikesRepository;
import com.example.fitnessrecord.community.routinepost.persist.RoutinePostRepository;
import com.example.fitnessrecord.global.exception.ErrorCode;
import com.example.fitnessrecord.global.exception.MyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class LikesServiceImpl implements LikesService {

  private final LikesRepository likesRepository;
  private final RoutinePostRepository routinePostRepository;

  @Override
  public LikesDto doLikes(Long userId, Long routinePostId) {
    boolean routinePostExists = routinePostRepository.existsById(routinePostId);
    boolean isAlreadyLikes = likesRepository.existsByUserIdAndRoutinePostId(userId, routinePostId);
    validation(routinePostExists, isAlreadyLikes);

    Likes saved = likesRepository.save(new Likes(userId, routinePostId));

    return LikesDto.fromEntity(saved);
  }

  @Override
  public LikesDto cancelLikes(Long userId, Long routinePostId) {
    Likes likes = likesRepository.findByUserIdAndRoutinePostId(userId, routinePostId)
        .orElseThrow(() -> new MyException(ErrorCode.LIKES_NOT_FOUND));

    LikesDto result = LikesDto.fromEntity(likes);

    likesRepository.delete(likes);

    return result;
  }

  private void validation(boolean routinePostExists, boolean isAlreadyLikes) {
    if (!routinePostExists) {
      throw new MyException(ErrorCode.ROUTINE_POST_NOT_FOUND);
    }
    if (isAlreadyLikes) {
      throw new MyException(ErrorCode.USER_ALREADY_LIKES_ROUTINE_POST);
    }
  }
}
