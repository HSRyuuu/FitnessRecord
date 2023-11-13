package com.example.fitnessrecord.domain.routine.element.service;

import com.example.fitnessrecord.domain.routine.element.dto.AddRoutineElementInput;
import com.example.fitnessrecord.domain.routine.element.dto.RoutineElementDto;
import java.util.List;

public interface RoutineElementService {

  /**
   * 루틴을 하나 추가한다.
   * @return 해당 루틴의 전체를 반환한다.
   */
  List<RoutineElementDto> addRoutineElement(AddRoutineElementInput input, Long userId);

  /**
   * 루틴을 하나 삭제한다.
   * @return 해당 루틴의 전체를 반환한다.
   */
  List<RoutineElementDto> deleteRoutineElement(Long routineElementId, Long userId);
}
