package com.example.fitnessrecord.domain.routine.routine.service;

import com.example.fitnessrecord.domain.routine.routine.dto.AddRoutineResult;

public interface RoutineService {

  /**
   * 루틴을 추가한다.
   */
  AddRoutineResult addRoutine(Long userId, String routineName);
}
