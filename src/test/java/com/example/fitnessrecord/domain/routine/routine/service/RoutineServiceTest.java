package com.example.fitnessrecord.domain.routine.routine.service;

import static org.assertj.core.api.Assertions.*;

import com.example.fitnessrecord.domain.routine.routine.dto.AddRoutineResult;
import com.example.fitnessrecord.domain.user.persist.User;
import com.example.fitnessrecord.domain.user.persist.UserRepository;
import com.example.fitnessrecord.global.exception.ErrorCode;
import com.example.fitnessrecord.global.exception.MyException;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@SpringBootTest
class RoutineServiceTest {

  @Autowired
  RoutineService routineService;

  static User user;

  @BeforeAll
  static void addUser(@Autowired UserRepository userRepository) {
    User input = User.builder()
        .email("test@test.com")
        .build();
    user = userRepository.save(input);
  }

  @AfterAll
  static void deleteUser(@Autowired UserRepository userRepository) {
    userRepository.delete(user);
  }

  @Nested
  @DisplayName("Routine 추가")
  class AddRoutine {

    @Test
    @DisplayName("성공")
    void addRoutine() {
      //given
      Long userId = user.getId();
      String routineName = "test";

      //when
      AddRoutineResult result = routineService.addRoutine(userId, routineName);

      //then
      assertThat(result.getRoutineName()).isEqualTo(routineName);
      assertThat(result.getUsername()).isEqualTo(user.getEmail());
    }

    @Test
    @DisplayName("실패: USER가 존재하지 않음")
    void addRoutine_USER_NOT_FOUND() {
      //given
      Long userId = user.getId();
      String routineName = "test";

      //when
      //then
      try{
        routineService.addRoutine(-1L , routineName);
      }catch(MyException e){
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
      }
    }
  }
}