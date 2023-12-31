package com.example.fitnessrecord.domain.record.trainingrecord.service;


import static org.assertj.core.api.Assertions.assertThat;

import com.example.fitnessrecord.domain.record.setrecord.dto.SetRecordDto;
import com.example.fitnessrecord.domain.record.setrecord.persist.SetRecord;
import com.example.fitnessrecord.domain.record.setrecord.persist.SetRecordRepository;
import com.example.fitnessrecord.domain.record.trainingrecord.dto.TrainingRecordDto;
import com.example.fitnessrecord.domain.record.trainingrecord.dto.TrainingRecordListResponse;
import com.example.fitnessrecord.domain.record.trainingrecord.dto.TrainingRecordResponse;
import com.example.fitnessrecord.domain.record.trainingrecord.persist.TrainingRecord;
import com.example.fitnessrecord.domain.record.trainingrecord.persist.TrainingRecordRepository;
import com.example.fitnessrecord.domain.training.common.type.BodyPart;
import com.example.fitnessrecord.domain.user.persist.User;
import com.example.fitnessrecord.domain.user.persist.UserRepository;
import com.example.fitnessrecord.global.exception.ErrorCode;
import com.example.fitnessrecord.global.exception.MyException;
import java.time.LocalDate;
import java.util.List;
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

  @Autowired
  TrainingRecordRepository trainingRecordRepository;

  @Autowired
  SetRecordRepository setRecordRepository;

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
      LocalDate today = LocalDate.now();

      //when
      TrainingRecordDto result = trainingRecordService.addTrainingRecord(userId, today);
      //then
      assertThat(result.getUsername()).isEqualTo(user.getEmail());
      assertThat(result.getDate()).isEqualTo(today);
    }

    @Test
    @DisplayName("성공: date == null 일 때")
    void addTrainingRecord_date_is_null() {
      //given
      Long userId = user.getId();
      LocalDate today = LocalDate.now();

      //when
      TrainingRecordDto result = trainingRecordService.addTrainingRecord(userId, null);
      //then
      assertThat(result.getUsername()).isEqualTo(user.getEmail());
      assertThat(result.getDate()).isEqualTo(today);
    }

    @Test
    @DisplayName("실패: 유저가 존재하지 않을 때")
    void addTrainingRecord_USER_NOT_FOUND() {
      //given
      Long userId = user.getId();
      LocalDate today = LocalDate.now();

      //when
      //then
      try {
        trainingRecordService.addTrainingRecord(-1L, today);
      } catch (MyException e) {
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
      }
    }
  }

  @Nested
  @DisplayName("TrainingRecord 조회")
  class GetTrainingRecordInfo {

    @Test
    @DisplayName("성공")
    void getTrainingRecordInfo() {
      //given
      TrainingRecord trainingRecord = trainingRecordRepository.save(TrainingRecord.builder()
          .user(user)
          .date(LocalDate.now())
          .build());

      Long trainingRecordId = trainingRecord.getId();
      String username = user.getEmail();

      //when
      TrainingRecordResponse response =
          trainingRecordService.getTrainingRecordInfo(trainingRecordId, username);
      TrainingRecordDto trainingRecordResponse = response.getTrainingRecord();
      List<SetRecordDto> setList = response.getSetList();

      //then
      assertThat(trainingRecordResponse.getId()).isEqualTo(trainingRecordId);
      assertThat(trainingRecordResponse.getUsername()).isEqualTo(username);
      for (SetRecordDto setRecord : setList) {
        assertThat(setRecord.getTrainingRecordId()).isEqualTo(trainingRecordId);
      }
    }

    @Test
    @DisplayName("실패: Training Record를 찾을 수 없음")
    void getTrainingRecordInfo_TRAINING_RECORD_NOT_FOUND() {
      //given
      TrainingRecord trainingRecord = trainingRecordRepository.save(TrainingRecord.builder()
          .user(user)
          .date(LocalDate.now())
          .build());

      Long trainingRecordId = trainingRecord.getId();
      String username = user.getEmail();

      //when
      //then
      try {
        trainingRecordService.getTrainingRecordInfo(-1L, username);
      } catch (MyException e) {
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.TRAINING_RECORD_NOT_FOUND);
      }
    }

    @Test
    @DisplayName("실패: 권한 없음(username이 틀림")
    void getTrainingRecordInfo_NO_AUTHORITY_ERROR() {
      //given
      TrainingRecord trainingRecord = trainingRecordRepository.save(TrainingRecord.builder()
          .user(user)
          .date(LocalDate.now())
          .build());

      Long trainingRecordId = trainingRecord.getId();
      String username = user.getEmail();

      //when
      //then
      try {
        trainingRecordService.getTrainingRecordInfo(trainingRecordId, "!wrong!");
      } catch (MyException e) {
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.NO_AUTHORITY_ERROR);
      }
    }
  }

  @Nested
  @DisplayName("일정 기간 내의 운동 기록 조회")
  class GetTrainingRecordList {

    @Test
    @DisplayName("성공")
    void getTrainingRecordList() {
      //given
      int trainingAmount = 3;
      Long trainingRecordId = 0L;
      for (int i = 0; i < trainingAmount; i++) {
        TrainingRecord saved = trainingRecordRepository.save(TrainingRecord.builder()
            .user(user)
            .date(LocalDate.now())
            .build());
        trainingRecordId = saved.getId();
        SetRecord saved1 = setRecordRepository.save(SetRecord.builder()
            .trainingRecord(saved)
            .trainingName("test")
            .user(user)
            .bodyPart(BodyPart.ETC)
            .date(LocalDate.now())
            .reps(10)
            .weight(100)
            .memo("test")
            .build());

      }

      Long userId = user.getId();
      LocalDate start = LocalDate.now();
      LocalDate end = LocalDate.now();

      //when
      TrainingRecordListResponse result =
          trainingRecordService.getTrainingRecordList(userId, 1, start, end);
      List<TrainingRecordResponse> list = result.getList();

      //then
      assertThat(list.size()).isEqualTo(trainingAmount);
      assertThat(result.getStart()).isEqualTo(start);
      assertThat(result.getEnd()).isEqualTo(end);

      for (TrainingRecordResponse response : list) {
        TrainingRecordDto trainingRecord = response.getTrainingRecord();
        assertThat(trainingRecord.getUsername()).isEqualTo(user.getEmail());
        assertThat(response.getSetList().get(0).getTrainingRecordId())
            .isEqualTo(trainingRecord.getId());
      }

    }

  }
}