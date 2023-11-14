package com.example.fitnessrecord.community.routinepost.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.fitnessrecord.community.routinepost.dto.AddRoutinePostInput;
import com.example.fitnessrecord.community.routinepost.dto.RoutinePostDto;
import com.example.fitnessrecord.domain.routine.element.dto.RoutineElementDto;
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
import org.junit.jupiter.api.AfterEach;
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
class RoutinePostServiceTest {

  @Autowired
  RoutinePostService routinePostService;

  static User user;
  static Routine routine;
  static int elementAmount = 5;

  @BeforeEach
  void addAllArgs(@Autowired UserRepository userRepository,
      @Autowired RoutineRepository routineRepository,
      @Autowired RoutineElementRepository routineElementRepository) {
    User input = User.builder()
        .email("test@test.com")
        .build();

    user = userRepository.save(input);

    Routine routineInput = Routine.builder()
        .user(user)
        .routineName("test")
        .lastModifiedDateTime(LocalDateTime.now())
        .description("test description")
        .build();
    routine = routineRepository.save(routineInput);

    for (int i = 1; i <= elementAmount; i++) {
      RoutineElement routineElement =
          RoutineElement.builder()
              .routine(routine)
              .orderNumber(i)
              .trainingName("test")
              .bodyPart(BodyPart.ETC)
              .reps(10)
              .build();
      routineElementRepository.save(routineElement);
    }

  }

  @AfterEach
  void deleteAllArgs(@Autowired UserRepository userRepository,
      @Autowired RoutineRepository routineRepository,
      @Autowired RoutineElementRepository routineElementRepository) {
    routineElementRepository.deleteAllByRoutine(routine);
    routineRepository.delete(routine);
    userRepository.delete(user);
  }

  @Nested
  @DisplayName("루틴 공유 게시글 쓰기")
  class AddRoutinePost {

    @Test
    @DisplayName("성공")
    void addRoutinePost() {
      //given
      AddRoutinePostInput input = AddRoutinePostInput.builder()
          .routineId(routine.getId())
          .title("test title")
          .content("test content")
          .build();

      //when
      RoutinePostDto result = routinePostService.addRoutinePost(input, user.getId());
      List<RoutineElementDto> resultElements = result.getElements();

      //then
      assertThat(result.getUserId()).isEqualTo(user.getId());
      assertThat(result.getRoutineId()).isEqualTo(routine.getId());
      assertThat(result.getTitle()).isEqualTo(input.getTitle());
      assertThat(result.getContent()).isEqualTo(input.getContent());
      assertThat(resultElements.size()).isEqualTo(elementAmount);
      for (int i = 1; i <= elementAmount; i++) {
        assertThat(resultElements.get(i - 1).getOrderNumber()).isEqualTo(i);
      }
    }

    @Test
    @DisplayName("실패: ROUTINE을 찾을 수 없음")
    void addRoutinePost_ROUTINE_NOT_FOUND() {
      //given
      AddRoutinePostInput input = AddRoutinePostInput.builder()
          .routineId(-1L)
          .title("test title")
          .content("test content")
          .build();

      //when
      assertThatThrownBy(() -> routinePostService.addRoutinePost(input, user.getId()))
          .isInstanceOf(MyException.class)
          .hasMessage(ErrorCode.ROUTINE_NOT_FOUND.getDescription());

    }

    @Test
    @DisplayName("실패: 권한 없음")
    void addRoutinePost_NO_AUTHORITY_ERROR() {
      //given
      AddRoutinePostInput input = AddRoutinePostInput.builder()
          .routineId(routine.getId())
          .title("test title")
          .content("test content")
          .build();

      //when
      assertThatThrownBy(() -> routinePostService.addRoutinePost(input, -1L))
          .isInstanceOf(MyException.class)
          .hasMessage(ErrorCode.NO_AUTHORITY_ERROR.getDescription());
    }

  }


}