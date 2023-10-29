package com.example.fitnessrecord.domain.record.trainingrecord.service;


import static org.assertj.core.api.Assertions.assertThat;

import com.example.fitnessrecord.domain.record.trainingrecord.dto.TrainingRecordDto;
import com.example.fitnessrecord.domain.user.persist.User;
import com.example.fitnessrecord.domain.user.persist.UserRepository;
import com.example.fitnessrecord.global.exception.ErrorCode;
import com.example.fitnessrecord.global.exception.MyException;
import java.time.LocalDate;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@Transactional
@SpringBootTest
class TrainingRecordServiceTest {

  @Autowired
  TrainingRecordService trainingRecordService;

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
  @DisplayName("Training Record 추가")
  class AddTrainingRecord {

    @Test
    @DisplayName("성공")
    void addTrainingRecord() {
      //given
      Long userId = user.getId();

      //when
      TrainingRecordDto result = trainingRecordService.addTrainingRecord(userId);
      //then
      assertThat(result.getUsername()).isEqualTo(user.getEmail());
      assertThat(result.getDate()).isEqualTo(LocalDate.now());
    }

    @Test
    @DisplayName("실패: 유저가 존재하지 않을 때")
    void addTrainingRecord_USER_NOT_FOUND() {
      //given
      Long userId = user.getId();

      //when
      //then
      try {
        trainingRecordService.addTrainingRecord(-1L);
      } catch (MyException e) {
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
      }
    }
  }
}