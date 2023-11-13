package com.example.fitnessrecord.domain.routine.element.service;

import com.example.fitnessrecord.domain.routine.element.dto.AddRoutineElementInput;
import com.example.fitnessrecord.domain.routine.element.dto.RoutineElementDto;
import com.example.fitnessrecord.domain.routine.element.dto.UpdateRoutineElementInput;
import com.example.fitnessrecord.domain.routine.element.persist.RoutineElement;
import com.example.fitnessrecord.domain.routine.element.persist.RoutineElementRepository;
import com.example.fitnessrecord.domain.routine.routine.persist.Routine;
import com.example.fitnessrecord.domain.routine.routine.persist.RoutineRepository;
import com.example.fitnessrecord.global.exception.ErrorCode;
import com.example.fitnessrecord.global.exception.MyException;
import java.time.LocalDateTime;
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

    this.validateAuthority(routine, userId);

    List<RoutineElement> list =
        routineElementRepository.findAllByRoutineIdOrderByOrderNumber(routine.getId());

    this.setOrderNumbersForAdd(list, input);

    list.add(AddRoutineElementInput.toEntity(input, routine));

    List<RoutineElement> saved = routineElementRepository.saveAll(list);

    this.updateLastModifiedTimeOfRoutine(routine);

    return entityListToSortedDtoList(saved);
  }

  /**
   * RoutineElement 추가 시 다른 모든 Element에 대한 OrderNumber 수정 ex) 1,2,3이 있을 경우 새로운 element가 2번으로 들어올 경우
   * 기존의 2번이 3번으로, 3번이 4번으로 가도록 함.
   */
  private void setOrderNumbersForAdd(List<RoutineElement> list, AddRoutineElementInput input) {
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

  @Override
  public List<RoutineElementDto> deleteRoutineElement(Long routineElementId, Long userId) {
    RoutineElement targetElement = routineElementRepository.findById(routineElementId)
        .orElseThrow(() -> new MyException(ErrorCode.ROUTINE_ELEMENT_NOT_FOUND));
    Routine routine = targetElement.getRoutine();

    this.validateAuthority(routine, userId);

    List<RoutineElement> list =
        routineElementRepository.findAllByRoutineIdOrderByOrderNumber(routine.getId());

    this.updateOthers(targetElement.getOrderNumber(), list); //삭제 대상 이외의 요소들 OrderNumber 수정

    this.deleteTargetElement(list, targetElement);//list와 db에서 targetElement 삭제

    List<RoutineElement> saved = routineElementRepository.saveAll(list);// wjwkd

    this.updateLastModifiedTimeOfRoutine(routine);

    return entityListToSortedDtoList(saved);
  }

  /**
   * 수정 시 targetElement 이외의 요소들 orderNumber 수정
   */
  private void updateOthers(int targetOrderNumber, List<RoutineElement> list) {
    for (int i = targetOrderNumber; i < list.size(); i++) {
      RoutineElement e = list.get(i);
      e.setOrderNumber(i);
    }
  }

  /**
   * Collection과 db에서 target 요소 삭제
   */
  private void deleteTargetElement(List<RoutineElement> list, RoutineElement targetElement) {
    list.remove(targetElement);
    routineElementRepository.delete(targetElement);
  }

  @Override
  public List<RoutineElementDto> updateRoutineElement(
      Long routineElementId, UpdateRoutineElementInput input, Long userId) {
    RoutineElement targetElement = routineElementRepository.findById(routineElementId)
        .orElseThrow(() -> new MyException(ErrorCode.ROUTINE_ELEMENT_NOT_FOUND));
    Routine routine = targetElement.getRoutine();

    this.validateAuthority(routine, userId);

    targetElement.update(input);

    routineElementRepository.save(targetElement);

    return this.entityListToSortedDtoList(
        routineElementRepository.findAllByRoutineIdOrderByOrderNumber(routine.getId()));
  }


  /**
   * 해당 루틴의 소유권을 가진 userId가 맞는지 검증
   */
  private void validateAuthority(Routine routine, Long userId) {
    if (!routine.getUser().getId().equals(userId)) {
      throw new MyException(ErrorCode.NO_AUTHORITY_ERROR);
    }
  }

  /**
   * routineElement 수정 시 routine의 lastModifiedDateTime 수정
   */
  private void updateLastModifiedTimeOfRoutine(Routine routine) {
    routine.setLastModifiedDateTime(LocalDateTime.now());
    routineRepository.save(routine);
  }

  /**
   * 저장된 entryList를 OrderNumber을 기준으로 정렬된 dtoList로 변환해서 반환
   */
  private List<RoutineElementDto> entityListToSortedDtoList(List<RoutineElement> entityList) {
    return entityList.stream()
        .map(RoutineElementDto::fromEntity)
        .sorted((o1, o2) -> o1.getOrderNumber() - o2.getOrderNumber())
        .collect(Collectors.toList());
  }
}
