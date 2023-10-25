package com.example.fitnessrecord.domain.training.custom.service;

import static org.assertj.core.api.Assertions.*;

import com.example.fitnessrecord.domain.training.common.type.BodyPart;
import com.example.fitnessrecord.domain.training.custom.dto.AddCustomTrainingInput;
import com.example.fitnessrecord.domain.training.custom.dto.CustomTrainingDto;
import com.example.fitnessrecord.domain.training.custom.persist.CustomTraining;
import com.example.fitnessrecord.domain.training.custom.persist.CustomTrainingRepository;
import com.example.fitnessrecord.domain.user.persist.User;
import com.example.fitnessrecord.domain.user.persist.UserRepository;
import com.example.fitnessrecord.global.exception.ErrorCode;
import com.example.fitnessrecord.global.exception.MyException;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
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
class CustomTrainingServiceTest {

  @Autowired
  UserRepository userRepository;
  @Autowired
  CustomTrainingService customTrainingService;
  @Autowired
  CustomTrainingRepository customTrainingRepository;

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
  @DisplayName("Custom 운동 추가")
  class AddCustomTraining {

    @Test
    @DisplayName("성공")
    void addCustomTraining() {
      //given
      AddCustomTrainingInput input = new AddCustomTrainingInput("customTest", BodyPart.ETC);

      //when
      CustomTrainingDto result =
          customTrainingService.addCustomTraining(user.getEmail(), input);

      CustomTraining saved =
          customTrainingRepository.findById(result.getId()).get();

      //then
      assertThat(saved.getTrainingName()).isEqualTo(result.getTrainingName());
      assertThat(saved.getBodyPart()).isEqualTo(result.getBodyPart());
    }

    @Test
    @DisplayName("실패: 유저를 찾을 수 없음")
    void addCustomTraining_USER_NOT_FOUND() {
      //given
      AddCustomTrainingInput input = new AddCustomTrainingInput("customTest", BodyPart.ETC);

      //when
      //then
      try{
        customTrainingService.addCustomTraining("!!wrongEmail", input);
      }catch(MyException e){
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
      }
    }

  }

  @Test
  void addCustomTraining() {
  }

  @Test
  void editCustomTraining() {
  }

  @Test
  void deleteCustomTraining() {
  }

  @Test
  void customTrainingList() {
  }
}