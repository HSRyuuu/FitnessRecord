package com.example.fitnessrecord.domain.record.setrecord.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.fitnessrecord.domain.record.setrecord.dto.AddSetRecordResult;
import com.example.fitnessrecord.domain.record.setrecord.dto.SetRecordInput;
import com.example.fitnessrecord.domain.record.trainingrecord.persist.TrainingRecord;
import com.example.fitnessrecord.domain.record.trainingrecord.persist.TrainingRecordRepository;
import com.example.fitnessrecord.domain.training.common.type.BodyPart;
import com.example.fitnessrecord.domain.user.persist.User;
import com.example.fitnessrecord.domain.user.persist.UserRepository;
import com.example.fitnessrecord.global.exception.ErrorCode;
import com.example.fitnessrecord.global.exception.MyException;
import java.time.LocalDate;
import java.util.ArrayList;
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
class SetRecordServiceTest {

  @Autowired
  private SetRecordService setRecordService;

  static User user;
  static TrainingRecord trainingRecord;

  @BeforeAll
  static void addTrainingRecordAndUser(@Autowired UserRepository userRepository,
      @Autowired TrainingRecordRepository trainingRecordRepository) {
    User uInput = User.builder()
        .email("test@test.com")
        .build();
    user = userRepository.save(uInput);

    TrainingRecord tInput = TrainingRecord.builder()
        .user(user)
        .date(LocalDate.now())
        .build();
    trainingRecord = trainingRecordRepository.save(tInput);
  }

  @AfterAll
  static void deleteTrainingRecordAndUser(@Autowired UserRepository userRepository,
      @Autowired TrainingRecordRepository trainingRecordRepository) {
    trainingRecordRepository.delete(trainingRecord);
    userRepository.delete(user);
  }

  @Nested
  @DisplayName("SetRecord 추가")
  class AddSetRecords {

    @Test
    @DisplayName("성공")
    void addSetRecords() {
      //given
      Long trainingRecordId = trainingRecord.getId();
      List<SetRecordInput> list = new ArrayList<>();
      int dataAmount = 3;
      for (int i = 0; i < dataAmount; i++) {
        SetRecordInput setRecord = SetRecordInput.builder()
            .bodyPart(BodyPart.ETC)
            .trainingName("test")
            .reps(10)
            .weight(10)
            .memo("test")
            .build();
        list.add(setRecord);
      }

      //when
      AddSetRecordResult result = setRecordService.addSetRecords(trainingRecordId, list);

      //then
      assertThat(result.getTrainingRecord().getId()).isEqualTo(trainingRecordId);
      assertThat(result.getAddAmount()).isEqualTo(dataAmount);
    }

    @Test
    @DisplayName("실패: Training Record가 존재하지 않음")
    void addSetRecords_TRAINING_RECORD_NOT_FOUND() {
      //given
      Long trainingRecordId = trainingRecord.getId();
      List<SetRecordInput> list = new ArrayList<>();
      int dataAmount = 3;
      for (int i = 0; i < dataAmount; i++) {
        SetRecordInput setRecord = SetRecordInput.builder()
            .bodyPart(BodyPart.ETC)
            .trainingName("test")
            .reps(10)
            .weight(10)
            .memo("test")
            .build();
        list.add(setRecord);
      }
      //when
      //then
      try{
        setRecordService.addSetRecords(-1L, list);
      }catch(MyException e){
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.TRAINING_RECORD_NOT_FOUND);
      }
    }

  }


}