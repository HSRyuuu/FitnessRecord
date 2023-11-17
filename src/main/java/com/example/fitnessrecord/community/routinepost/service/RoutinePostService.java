package com.example.fitnessrecord.community.routinepost.service;

import com.example.fitnessrecord.community.routinepost.dto.AddRoutinePostInput;
import com.example.fitnessrecord.community.routinepost.dto.DeleteRoutinePostResult;
import com.example.fitnessrecord.community.routinepost.dto.RoutinePostDto;
import com.example.fitnessrecord.community.routinepost.dto.RoutinePostResult;
import com.example.fitnessrecord.community.routinepost.dto.UpdateRoutinePostInput;
import com.example.fitnessrecord.domain.routine.routine.dto.RoutineDto;

public interface RoutinePostService {

  /**
   * 루틴 공유 게시글 쓰기
   */
  RoutinePostResult addRoutinePost(AddRoutinePostInput input, Long userId);

  /**
   * 게시글 조회
   */
  RoutinePostDto getRoutinePost(Long id, Long userId);

  /**
   * 조회수 1 증가
   */
  boolean addView(Long userId, Long routinePostId);

  /**
   * 좋아요 1 증가
   */
  void addLikes(Long routinePostId);

  /**
   * 좋아요 1 감소
   */
  void cancelLikes(Long routinePostId);

  /**
   * 게시글 수정
   */
  RoutinePostResult updateRoutinePost(Long id, UpdateRoutinePostInput input, Long userId);

  /**
   * 게시글 삭제
   */
  DeleteRoutinePostResult deleteRoutinePost(Long id, Long userId);

  /**
   * 다른 사람의 루틴을 나의 루틴으로 저장
   */
  RoutineDto quoteAndSaveRoutine(Long routinePostId, Long userId);
}

