package com.example.fitnessrecord.domain.training.custom.service;

import static org.assertj.core.api.Assertions.*;

import com.example.fitnessrecord.domain.training.common.type.BodyPart;
import com.example.fitnessrecord.domain.training.custom.dto.AddCustomTrainingInput;
import com.example.fitnessrecord.domain.training.custom.dto.CustomTrainingDto;
import com.example.fitnessrecord.domain.training.custom.dto.EditCustomTrainingInput;
import com.example.fitnessrecord.domain.training.custom.persist.CustomTraining;
import com.example.fitnessrecord.domain.training.custom.persist.CustomTrainingRepository;
import com.example.fitnessrecord.domain.user.persist.User;
import com.example.fitnessrecord.domain.user.persist.UserRepository;
import com.example.fitnessrecord.global.exception.ErrorCode;
import com.example.fitnessrecord.global.exception.MyException;
import java.util.Optional;
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
  @Nested
  @DisplayName("Custom Training 수정")
  class EditCustomTraining{
    @Test
    @DisplayName("성공")
    void editCustomTraining(){
      //given
      String username = user.getEmail();
      CustomTraining customTraining = CustomTraining.builder()
          .user(user)
          .trainingName("before")
          .bodyPart(BodyPart.ETC)
          .build();
      CustomTraining savedCustomTraining = customTrainingRepository.save(customTraining);

      EditCustomTrainingInput input =
          new EditCustomTrainingInput(savedCustomTraining.getId(), "after", BodyPart.ABS);

      //when
      CustomTrainingDto result =
          customTrainingService.editCustomTraining(username, input);

      //then
      assertThat(result.getTrainingName()).isEqualTo(input.getTrainingName());
      assertThat(result.getBodyPart()).isEqualTo(input.getBodyPart());
    }

    @Test
    @DisplayName("실패: 해당 Custom Training 이 존재하지 않음")
    void editCustomTraining_TRAINING_NOT_FOUND_BY_ID(){
      //given
      String username = user.getEmail();
      CustomTraining customTraining = CustomTraining.builder()
          .user(user)
          .trainingName("before")
          .bodyPart(BodyPart.ETC)
          .build();
      CustomTraining savedCustomTraining = customTrainingRepository.save(customTraining);

      EditCustomTrainingInput input =
          new EditCustomTrainingInput(-1L, "after", BodyPart.ABS);

      //when
      //then
      try{
        customTrainingService.editCustomTraining(username, input);
      }catch(MyException e){
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.TRAINING_NOT_FOUND_BY_ID);
      }
    }

    @Test
    @DisplayName("실패: 권한 없음 - 로그인 유저의 Custom Training이 아님")
    void editCustomTraining_NO_AUTHORITY_ERROR(){
      //given
      String username = user.getEmail();
      CustomTraining customTraining = CustomTraining.builder()
          .user(user)
          .trainingName("before")
          .bodyPart(BodyPart.ETC)
          .build();
      CustomTraining savedCustomTraining = customTrainingRepository.save(customTraining);

      EditCustomTrainingInput input =
          new EditCustomTrainingInput(savedCustomTraining.getId(), "after", BodyPart.ABS);

      //when
      //then
      try{
        customTrainingService.editCustomTraining("!wrongUser!", input);
      }catch(MyException e){
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.NO_AUTHORITY_ERROR);
      }
    }

  }

  @Nested
  @DisplayName("Custom Training 삭제")
  class DeleteCustomTraining{
    @Test
    @DisplayName("성공")
    void deleteCustomTraining(){
      //given
      String username = user.getEmail();
      CustomTraining customTraining = CustomTraining.builder()
          .user(user)
          .trainingName("before")
          .bodyPart(BodyPart.ETC)
          .build();
      CustomTraining savedCustomTraining = customTrainingRepository.save(customTraining);

      //when
      CustomTrainingDto result = customTrainingService.deleteCustomTraining(username,
          savedCustomTraining.getId());

      //then
      assertThat(result.getTrainingName()).isEqualTo(savedCustomTraining.getTrainingName());
      assertThat(result.getBodyPart()).isEqualTo(savedCustomTraining.getBodyPart());
      assertThat(result.getUsername()).isEqualTo(username);
      assertThat(customTrainingRepository.findById(customTraining.getId()))
          .isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("실패: 해당 ID의 Custom Training이 존재하지 않음")
    void deleteCustomTraining_TRAINING_NOT_FOUND_BY_ID(){
      //given
      String username = user.getEmail();
      //when
      //then
      try{
        customTrainingService.deleteCustomTraining(username, -1L);
      }catch(MyException e){
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.TRAINING_NOT_FOUND_BY_ID);
      }
    }

    @Test
    @DisplayName("실패: 권한 없음 - 해당 Custom Training의 삭제 권한 없음")
    void deleteCustomTraining_NO_AUTHORITY_ERROR(){
      //given
      String username = user.getEmail();
      CustomTraining customTraining = CustomTraining.builder()
          .user(user)
          .trainingName("before")
          .bodyPart(BodyPart.ETC)
          .build();
      CustomTraining savedCustomTraining = customTrainingRepository.save(customTraining);
      //when
      //then
      try{
        customTrainingService.deleteCustomTraining("!wrongUser!", savedCustomTraining.getId());
      }catch(MyException e){
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.NO_AUTHORITY_ERROR);
      }
    }


  }
  @Test
  void deleteCustomTraining() {
  }

  @Test
  void customTrainingList() {
  }
}