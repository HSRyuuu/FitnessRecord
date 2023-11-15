package com.example.fitnessrecord.community.routinepost.service;

import com.example.fitnessrecord.community.routinepost.dto.AddRoutinePostInput;
import com.example.fitnessrecord.community.routinepost.dto.DeleteRoutinePostResult;
import com.example.fitnessrecord.community.routinepost.dto.RoutinePostDto;
import com.example.fitnessrecord.community.routinepost.dto.RoutinePostResult;
import com.example.fitnessrecord.community.routinepost.dto.UpdateRoutinePostInput;

public interface RoutinePostService {

  /**
   * 루틴 공유 게시글 쓰기
   */
  RoutinePostResult addRoutinePost(AddRoutinePostInput input, Long userId);

  /**
   * 게시글 조회
   */
  RoutinePostDto getRoutinePost(Long id);

  /**
   * 게시글 수정
   */
  RoutinePostResult updateRoutinePost(Long id, UpdateRoutinePostInput input, Long userId);

  /**
   * 게시글 삭제
   */
  DeleteRoutinePostResult deleteRoutinePost(Long id, Long userId);
}

