package com.example.fitnessrecord.domain.routine.element.service;

import com.example.fitnessrecord.domain.routine.element.dto.AddRoutineElementInput;
import com.example.fitnessrecord.domain.routine.element.dto.RoutineElementDto;
import com.example.fitnessrecord.domain.routine.element.persist.RoutineElement;
import com.example.fitnessrecord.domain.routine.element.persist.RoutineElementRepository;
import com.example.fitnessrecord.domain.routine.routine.persist.Routine;
import com.example.fitnessrecord.domain.routine.routine.persist.RoutineRepository;
import com.example.fitnessrecord.global.exception.ErrorCode;
import com.example.fitnessrecord.global.exception.MyException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoutineElementServiceImpl implements RoutineElementService {

  private final RoutineRepository routineRepository;
  private final RoutineElementRepository routineElementRepository;

  @Override
  public List<RoutineElementDto> addRoutineElement(AddRoutineElementInput input, Long userId) {
    Routine routine = routineRepository.findById(input.getRoutineId())
        .orElseThrow(() -> new MyException(ErrorCode.ROUTINE_NOT_FOUND));

    if (!routine.getUser().getId().equals(userId)) {
      throw new MyException(ErrorCode.NO_AUTHORITY_ERROR);
    }

    List<RoutineElement> list =
        routineElementRepository.findAllByRoutineIdOrderByOrderNumber(routine.getId());

    this.setOrderNumbers(list, input);

    list.add(AddRoutineElementInput.toEntity(input, routine));

    List<RoutineElement> saved = routineElementRepository.saveAll(list);

    return saved.stream()
        .map(RoutineElementDto::fromEntity)
        .sorted((o1, o2) -> o1.getOrderNumber() - o2.getOrderNumber())
        .collect(Collectors.toList());
  }

  private void setOrderNumbers(List<RoutineElement> list, AddRoutineElementInput input) {
    int orderNumber = input.getOrderNumber();
    //리스트가 비어있을 경우 무조건 1번부터
    if (list.isEmpty()) {
      input.setOrderNumber(1);
    } else if (orderNumber > list.size()) {
      // 현재 최대 순서 + 1보다 클 경우 바로 다음 수로 변경
      input.setOrderNumber(list.size() + 1);
    } else {
      // 중간에 끼는 경우
      for (int i = list.size() - 1; i >= orderNumber - 1; i--) {
        RoutineElement routineElement = list.get(i);
        routineElement.setOrderNumber(routineElement.getOrderNumber() + 1);
      }
    }

  }
}
