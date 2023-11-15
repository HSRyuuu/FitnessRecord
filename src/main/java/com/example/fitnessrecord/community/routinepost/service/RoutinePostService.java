package com.example.fitnessrecord.community.routinepost.service;

import com.example.fitnessrecord.community.routinepost.dto.AddRoutinePostInput;
import com.example.fitnessrecord.community.routinepost.dto.RoutinePostDto;
import com.example.fitnessrecord.community.routinepost.dto.RoutinePostResult;

public interface RoutinePostService {

  /**
   * 루틴 공유 게시글 쓰기
   */
  RoutinePostResult addRoutinePost(AddRoutinePostInput input, Long userId);

  /**
   * 게시글 조회
   */
  RoutinePostDto getRoutinePost(Long id);
}

