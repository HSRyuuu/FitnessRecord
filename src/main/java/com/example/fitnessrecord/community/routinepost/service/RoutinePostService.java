package com.example.fitnessrecord.community.routinepost.service;

import com.example.fitnessrecord.community.routinepost.dto.AddRoutinePostInput;
import com.example.fitnessrecord.community.routinepost.dto.RoutinePostDto;

public interface RoutinePostService {

  /**
   * 루틴 공유 게시글 쓰기
   */
  RoutinePostDto addRoutinePost(AddRoutinePostInput input, Long userId);
  }

