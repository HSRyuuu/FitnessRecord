package com.example.fitnessrecord.domain.routine.element.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.fitnessrecord.domain.routine.element.dto.AddRoutineElementInput;
import com.example.fitnessrecord.domain.routine.element.dto.RoutineElementDto;
import com.example.fitnessrecord.domain.routine.element.dto.UpdateRoutineElementInput;
import com.example.fitnessrecord.domain.routine.element.persist.RoutineElement;
import com.example.fitnessrecord.domain.routine.element.persist.RoutineElementRepository;
import com.example.fitnessrecord.domain.routine.routine.persist.Routine;
import com.example.fitnessrecord.domain.routine.routine.persist.RoutineRepository;
import com.example.fitnessrecord.domain.training.common.type.BodyPart;
import com.example.fitnessrecord.domain.user.persist.User;
import com.example.fitnessrecord.domain.user.persist.UserRepository;
import com.example.fitnessrecord.global.exception.ErrorCode;
import com.example.fitnessrecord.global.exception.MyException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@SpringBootTest
class RoutineElementServiceTest {

  @Autowired
  RoutineElementService routineElementService;
  @Autowired
  RoutineElementRepository routineElementRepository;

  static User user;
  static Routine routine;

  @BeforeAll
  static void addUser(@Autowired UserRepository userRepository,
      @Autowired RoutineRepository routineRepository) {
    User inputUser = User.builder()
        .email("test@test.com")
        .build();
    user = userRepository.save(inputUser);

    Routine inputRoutine = Routine.builder()
        .user(user)
        .routineName("test")
        .lastModifiedDateTime(LocalDateTime.now())
        .build();

    routine = routineRepository.save(inputRoutine);


  }

  @AfterAll
  static void deleteUser(@Autowired UserRepository userRepository,
      @Autowired RoutineRepository routineRepository) {
    routineRepository.delete(routine);
    userRepository.delete(user);
  }

  @Nested
  @DisplayName("루틴 요소 추가하기")
  class AddRoutineElement {

    @Test
    @DisplayName("성공")
    void addRoutineElement() {
      //given
      AddRoutineElementInput input = AddRoutineElementInput.builder()
          .routineId(routine.getId())
          .orderNumber(1)
          .trainingName("test")
          .bodyPart(BodyPart.ETC)
          .reps(10)
          .build();
      Long userId = user.getId();

      //when
      List<RoutineElementDto> resultList =
          routineElementService.addRoutineElement(input, userId);

      //then
      RoutineElementDto result = resultList.get(0);
      assertThat(result.getRoutineId()).isEqualTo(routine.getId());
      assertThat(result.getOrderNumber()).isEqualTo(input.getOrderNumber());
      assertThat(result.getTrainingName()).isEqualTo(input.getTrainingName());
      assertThat(result.getBodyPart()).isEqualTo(input.getBodyPart());
      assertThat(result.getReps()).isEqualTo(input.getReps());
    }

    @Test
    @DisplayName("성공: 첫번째 요소를 넣는데 orderNumber가 1이 아닌 경우")
    void addRoutineElement_addFirst() {
      //given
      AddRoutineElementInput input = AddRoutineElementInput.builder()
          .routineId(routine.getId())
          .orderNumber(1)
          .trainingName("test")
          .bodyPart(BodyPart.ETC)
          .reps(10)
          .build();

      Long userId = user.getId();

      //when
      List<RoutineElementDto> resultList =
          routineElementService.addRoutineElement(input, userId);

      //then
      RoutineElementDto result = resultList.get(0);
      assertThat(result.getRoutineId()).isEqualTo(routine.getId());
      assertThat(result.getOrderNumber()).isEqualTo(input.getOrderNumber());
      assertThat(result.getTrainingName()).isEqualTo(input.getTrainingName());
      assertThat(result.getBodyPart()).isEqualTo(input.getBodyPart());
      assertThat(result.getReps()).isEqualTo(input.getReps());
    }

    @Test
    @DisplayName("성공: 전체 루틴의 최대 순서보다 큰 수를 넣을 때")
    void addRoutineElement_addLast() {
      //given
      int listSize = 5;
      for (int i = 0; i < 5; i++) {
        AddRoutineElementInput input = AddRoutineElementInput.builder()
            .routineId(routine.getId())
            .orderNumber(i + 1)
            .trainingName("test")
            .bodyPart(BodyPart.ETC)
            .reps(10)
            .build();
        routineElementRepository.save(AddRoutineElementInput.toEntity(input, routine));
      }

      AddRoutineElementInput input = AddRoutineElementInput.builder()
          .routineId(routine.getId())
          .orderNumber(listSize + 5)
          .trainingName("test")
          .bodyPart(BodyPart.ETC)
          .reps(10)
          .build();

      Long userId = user.getId();

      //when
      List<RoutineElementDto> resultList =
          routineElementService.addRoutineElement(input, userId);

      //then
      RoutineElementDto result = resultList.get(listSize);//맨 뒤의 result를 꺼냄
      assertThat(result.getRoutineId()).isEqualTo(routine.getId());
      assertThat(result.getOrderNumber()).isEqualTo(listSize + 1);
      assertThat(result.getTrainingName()).isEqualTo(input.getTrainingName());
      assertThat(result.getBodyPart()).isEqualTo(input.getBodyPart());
      assertThat(result.getReps()).isEqualTo(input.getReps());
    }

    @Test
    @DisplayName("성공: 전체 루틴의 중간에 넣을 때")
    void addRoutineElement_addMiddle() {
      //given
      int listSize = 5;
      for (int i = 0; i < 5; i++) {
        AddRoutineElementInput input = AddRoutineElementInput.builder()
            .routineId(routine.getId())
            .orderNumber(i + 1)
            .trainingName("test")
            .bodyPart(BodyPart.ETC)
            .reps(10)
            .build();
        routineElementRepository.save(AddRoutineElementInput.toEntity(input, routine));
      }
      int testOrder = 2;
      String testName = "insert";
      AddRoutineElementInput input = AddRoutineElementInput.builder()
          .routineId(routine.getId())
          .orderNumber(testOrder)
          .trainingName("insert")
          .bodyPart(BodyPart.ETC)
          .reps(10)
          .build();

      Long userId = user.getId();

      //when
      List<RoutineElementDto> resultList =
          routineElementService.addRoutineElement(input, userId);

      //then
      assertThat(resultList.size()).isEqualTo(6);
      for (int i = 0; i < resultList.size(); i++) {
        assertThat(resultList.get(i).getOrderNumber()).isEqualTo(i + 1);
      }
      assertThat(resultList.get(testOrder - 1).getOrderNumber()).isEqualTo(testOrder);
      assertThat(resultList.get(testOrder - 1).getTrainingName()).isEqualTo(testName);
    }

    @Test
    @DisplayName("실패: routine을 찾을 수 없음")
    void addRoutineElement_ROUTINE_NOT_FOUND() {
      //given
      AddRoutineElementInput input = AddRoutineElementInput.builder()
          .routineId(-1L)
          .orderNumber(1)
          .trainingName("test")
          .bodyPart(BodyPart.ETC)
          .reps(10)
          .build();
      Long userId = user.getId();

      //when
      //then
      try {
        routineElementService.addRoutineElement(input, userId);
      } catch (MyException e) {
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.ROUTINE_NOT_FOUND);
      }
    }

    @Test
    @DisplayName("실패: 권한 없음")
    void addRoutineElement_NO_AUTHORITY_ERROR() {
      //given
      AddRoutineElementInput input = AddRoutineElementInput.builder()
          .routineId(routine.getId())
          .orderNumber(1)
          .trainingName("test")
          .bodyPart(BodyPart.ETC)
          .reps(10)
          .build();
      Long userId = -1L;

      //when
      //then
      try {
        routineElementService.addRoutineElement(input, userId);
      } catch (MyException e) {
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.NO_AUTHORITY_ERROR);
      }
    }
  }

  @Nested
  @DisplayName("루틴 요소 삭제")
  class DeleteRoutineElement {

    static final int listSize = 5;

    @BeforeEach
    void addElements() {
      for (int i = 1; i <= listSize; i++) {
        routineElementRepository.save(
            RoutineElement.builder()
                .routine(routine)
                .orderNumber(i)
                .trainingName("test")
                .bodyPart(BodyPart.CHEST)
                .reps(10)
                .build());
      }
    }

    @Test
    @DisplayName("성공_중간 요소 삭제")
    void deleteRoutineElement_middle() {
      //given
      List<RoutineElement> routineElements =
          routineElementRepository.findAllByRoutineIdOrderByOrderNumber(routine.getId());

      int targetOrderNumber = 3;
      RoutineElement targetElement = routineElements.get(targetOrderNumber - 1);
      Long routineElementId = targetElement.getId();

      //when
      routineElementService.deleteRoutineElement(routineElementId, user.getId());

      List<RoutineElement> afterList =
          routineElementRepository.findAllByRoutineIdOrderByOrderNumber(routine.getId());

      //then
      assertThat(afterList.size()).isEqualTo(listSize - 1);
      for (int i = 1; i < afterList.size(); i++) {
        assertThat(afterList.get(i - 1).getOrderNumber()).isEqualTo(i);
      }
    }

    @Test
    @DisplayName("성공_첫번째 요소 삭제")
    void deleteRoutineElement_first() {
      //given
      List<RoutineElement> routineElements =
          routineElementRepository.findAllByRoutineIdOrderByOrderNumber(routine.getId());

      int targetOrderNumber = 1;
      RoutineElement targetElement = routineElements.get(targetOrderNumber - 1);
      Long routineElementId = targetElement.getId();

      //when
      routineElementService.deleteRoutineElement(routineElementId, user.getId());

      List<RoutineElement> afterList =
          routineElementRepository.findAllByRoutineIdOrderByOrderNumber(routine.getId());

      //then
      assertThat(afterList.size()).isEqualTo(listSize - 1);
      for (int i = 1; i < afterList.size(); i++) {
        assertThat(afterList.get(i - 1).getOrderNumber()).isEqualTo(i);
      }
    }

    @Test
    @DisplayName("성공_마지막 요소 삭제")
    void deleteRoutineElement_last() {
      //given
      List<RoutineElement> routineElements =
          routineElementRepository.findAllByRoutineIdOrderByOrderNumber(routine.getId());

      int targetOrderNumber = listSize;
      RoutineElement targetElement = routineElements.get(targetOrderNumber - 1);
      Long routineElementId = targetElement.getId();

      //when
      routineElementService.deleteRoutineElement(routineElementId, user.getId());

      List<RoutineElement> afterList =
          routineElementRepository.findAllByRoutineIdOrderByOrderNumber(routine.getId());

      //then
      assertThat(afterList.size()).isEqualTo(listSize - 1);
      for (int i = 1; i < afterList.size(); i++) {
        assertThat(afterList.get(i - 1).getOrderNumber()).isEqualTo(i);
      }
    }

    @Test
    @DisplayName("실패: RoutineElementId 틀림 or 존재하지 않음")
    void deleteRoutineElement_ROUTINE_ELEMENT_NOT_FOUND() {
      //given
      //when
      try {
        routineElementService.deleteRoutineElement(-1L, user.getId());
      } catch (MyException e) {
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.ROUTINE_ELEMENT_NOT_FOUND);
      }
    }

    @Test
    @DisplayName("실패: 권한 없음")
    void deleteRoutineElement_NO_AUTHORITY_ERROR() {
      //given
      List<RoutineElement> routineElements =
          routineElementRepository.findAllByRoutineIdOrderByOrderNumber(routine.getId());

      int targetOrderNumber = 2;
      RoutineElement targetElement = routineElements.get(targetOrderNumber - 1);
      Long routineElementId = targetElement.getId();

      //when
      try {
        routineElementService.deleteRoutineElement(routineElementId, -1L);
      } catch (MyException e) {
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.NO_AUTHORITY_ERROR);
      }
    }

  }

  @Nested
  @DisplayName("루틴 요소 수정")
  class UpdateRoutineElement{

    static final int listSize = 5;

    @BeforeEach
    void addElements() {
      for (int i = 1; i <= listSize; i++) {
        routineElementRepository.save(
            RoutineElement.builder()
                .routine(routine)
                .orderNumber(i)
                .trainingName("test")
                .bodyPart(BodyPart.CHEST)
                .reps(10)
                .build());
      }
    }

    @Test
    @DisplayName("성공")
    void updateRoutineElement(){
      //given
      UpdateRoutineElementInput input = UpdateRoutineElementInput.builder()
          .trainingName("update")
          .bodyPart(BodyPart.ETC)
          .reps(100)
          .build();

      List<RoutineElement> routineElements =
          routineElementRepository.findAllByRoutineIdOrderByOrderNumber(routine.getId());

      int targetOrderNumber = 2;
      RoutineElement targetElement = routineElements.get(targetOrderNumber - 1);
      Long routineElementId = targetElement.getId();

      //when
      List<RoutineElementDto> updatedList = routineElementService.updateRoutineElement(
          routineElementId, input, user.getId());
      RoutineElementDto updatedRoutineElement = updatedList.get(targetOrderNumber - 1);

      //then
      assertThat(updatedRoutineElement.getOrderNumber()).isEqualTo(targetOrderNumber);
      assertThat(updatedRoutineElement.getTrainingName()).isEqualTo(input.getTrainingName());
      assertThat(updatedRoutineElement.getBodyPart()).isEqualTo(input.getBodyPart());
      assertThat(updatedRoutineElement.getReps()).isEqualTo(input.getReps());
    }

    @Test
    @DisplayName("실패: RoutineElementId 틀림 or 존재하지 않음")
    void updateRoutineElement_ROUTINE_ELEMENT_NOT_FOUND() {
      //given
      //when
      try {
        routineElementService.updateRoutineElement(-1L, new UpdateRoutineElementInput(), user.getId());
      } catch (MyException e) {
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.ROUTINE_ELEMENT_NOT_FOUND);
      }
    }

    @Test
    @DisplayName("실패: 권한 없음")
    void updateRoutineElement_NO_AUTHORITY_ERROR() {
      //given
      List<RoutineElement> routineElements =
          routineElementRepository.findAllByRoutineIdOrderByOrderNumber(routine.getId());

      int targetOrderNumber = 2;
      RoutineElement targetElement = routineElements.get(targetOrderNumber - 1);
      Long routineElementId = targetElement.getId();

      //when
      try {
        routineElementService.updateRoutineElement(routineElementId, new UpdateRoutineElementInput(), -1L);
      } catch (MyException e) {
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.NO_AUTHORITY_ERROR);
      }
    }

  }

}