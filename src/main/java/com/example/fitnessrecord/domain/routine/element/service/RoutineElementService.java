package com.example.fitnessrecord.domain.routine.element.service;

import com.example.fitnessrecord.domain.routine.element.dto.AddRoutineElementInput;
import com.example.fitnessrecord.domain.routine.element.dto.RoutineElementDto;
import com.example.fitnessrecord.domain.routine.element.dto.UpdateRoutineElementInput;
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

  /**
   * 루틴을 수정한다.
   * - OrderNumber는 수정 불가. 삭제 후 추가하는 방식으로 수정할 수 있음
   * @return 해당 루틴의 전체를 반환한다.
   */
  List<RoutineElementDto> updateRoutineElement(Long routineElementId, UpdateRoutineElementInput input,  Long userId);

}
