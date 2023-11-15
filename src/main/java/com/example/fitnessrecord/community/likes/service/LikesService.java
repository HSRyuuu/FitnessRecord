package com.example.fitnessrecord.community.likes.service;

import com.example.fitnessrecord.community.likes.dto.LikesDto;

public interface LikesService {

  /**
   * 좋아요(likes) 누름
   */
  LikesDto doLikes(Long userId, Long routinePostId);

  /**
   * 좋아요(likes) 취소
   */
  LikesDto cancelLikes(Long userId, Long routinePostId);
}
