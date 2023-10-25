package com.example.fitnessrecord.domain.training.basic.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.fitnessrecord.domain.training.basic.dto.AddBasicTrainingInput;
import com.example.fitnessrecord.domain.training.basic.dto.BasicTrainingDto;
import com.example.fitnessrecord.domain.training.basic.dto.EditBasicTrainingInput;
import com.example.fitnessrecord.domain.training.basic.persist.BasicTraining;
import com.example.fitnessrecord.domain.training.basic.persist.BasicTrainingRepository;
import com.example.fitnessrecord.domain.training.common.type.BodyPart;
import com.example.fitnessrecord.global.exception.ErrorCode;
import com.example.fitnessrecord.global.exception.MyException;
import java.util.List;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@Transactional
@SpringBootTest
class AdminTrainingServiceTest {

  @Autowired
  AdminTrainingService adminTrainingService;

  @Autowired
  BasicTrainingRepository basicTrainingRepository;

  @Nested
  @DisplayName("ADMIN - BasicTraining 추가")
  class AddTraining {

    @Test
    @DisplayName("성공")
    void addTraining() {
      //given
      AddBasicTrainingInput input = AddBasicTrainingInput
          .builder()
          .trainingName("test")
          .trainingNameKor("테스트")
          .bodyPart(BodyPart.ETC)
          .build();

      //when
      BasicTrainingDto addedTraining = adminTrainingService.addTraining(input);

      //then
      assertThat(addedTraining.getId()).isNotNull();
      assertThat(addedTraining.getTrainingName()).isEqualTo(input.getTrainingName());
      assertThat(addedTraining.getTrainingNameKor()).isEqualTo(input.getTrainingNameKor());
      assertThat(addedTraining.getBodyPart()).isEqualTo(input.getBodyPart());
    }

    @Test
    @DisplayName("실패: 이미 존재 하는 Training name")
    void addTraining_TRAINING_NAME_ALREADY_EXIST() {
      //given
      BasicTraining saved = basicTrainingRepository.save(BasicTraining.builder()
          .trainingName("test")
          .trainingNameKor("테스트")
          .bodyPart(BodyPart.ETC)
          .build());

      AddBasicTrainingInput input = AddBasicTrainingInput
          .builder()
          .trainingName("test")
          .trainingNameKor("테스트")
          .bodyPart(BodyPart.ETC)
          .build();

      //when
      //then
      try {
        adminTrainingService.addTraining(input);
      } catch (MyException e) {
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.TRAINING_NAME_ALREADY_EXIST);
      }
    }
  }

  @Nested
  @DisplayName("ADMIN - Basic Training 삭제")
  class DeleteTraining {

    @Test
    @DisplayName("성공")
    void deleteTraining() {
      String name = "test";
      //given
      BasicTraining saved = basicTrainingRepository.save(BasicTraining.builder()
          .trainingName(name)
          .trainingNameKor("테스트")
          .bodyPart(BodyPart.ETC)
          .build());

      //when
      BasicTrainingDto deleted = adminTrainingService.deleteTraining(name);

      //then
      assertThat(basicTrainingRepository.existsByTrainingName(name)).isFalse();
      assertThat(deleted.getId()).isEqualTo(saved.getId());
      assertThat(deleted.getTrainingName()).isEqualTo(saved.getTrainingName());
      assertThat(deleted.getBodyPart()).isEqualTo(saved.getBodyPart());
    }

    @Test
    @DisplayName("실패: 존재하지 않는 TrainingName")
    void deleteTraining_TRAINING_NAME_NOT_FOUND() {
      //given
      String name = "!none";

      //when
      //then
      try {
        adminTrainingService.deleteTraining(name);
      } catch (MyException e) {
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.TRAINING_NOT_FOUND_BY_ID);
      }
    }

  }

  @Nested
  @DisplayName("ADMIN - Basic Training 수정")
  class UpdateTraining {

    @Test
    @DisplayName("성공")
    void updateTraining() {
      String nameBefore = "test";
      String korBefore = "테스트";
      String nameAfter = "after";
      String korAfter = "수정";
      BodyPart bodyPartBefore = BodyPart.BICEPS;
      BodyPart bodyPartAfter = BodyPart.ETC;
      //given
      BasicTraining saved = basicTrainingRepository.save(BasicTraining.builder()
          .trainingName(nameBefore)
          .trainingNameKor(korBefore)
          .bodyPart(bodyPartBefore)
          .build());

      EditBasicTrainingInput input = EditBasicTrainingInput.builder()
          .id(saved.getId())
          .trainingName(nameAfter)
          .trainingNameKor(korAfter)
          .bodyPart(bodyPartAfter)
          .build();

      //when
      adminTrainingService.updateTraining(input);

      BasicTraining updated = basicTrainingRepository.findById(saved.getId()).get();

      //then
      assertThat(updated.getTrainingName()).isEqualTo(nameAfter);
      assertThat(updated.getTrainingNameKor()).isEqualTo(korAfter);
      assertThat(updated.getBodyPart()).isEqualTo(bodyPartAfter);
    }

    @Test
    @DisplayName("실패: id가 일치하는 Training이 존재하지 않음")
    void updateTraining_TRAINING_NAME_NOT_FOUND() {
      //given
      BasicTraining saved = basicTrainingRepository.save(BasicTraining.builder()
          .trainingName("test")
          .trainingNameKor("테스트")
          .bodyPart(BodyPart.ETC)
          .build());

      EditBasicTrainingInput input = EditBasicTrainingInput.builder()
          .id(saved.getId() + 100)
          .trainingName("after")
          .trainingNameKor("after")
          .bodyPart(BodyPart.ETC)
          .build();

      //when
      //then
      try {
        adminTrainingService.updateTraining(input);
      } catch (MyException e) {
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.TRAINING_NOT_FOUND_BY_NAME);
      }
    }

  }

  @Nested
  @DisplayName("ADMIN - Basic Training 리스트")
  class TrainingList {

    @Test
    @DisplayName("성공")
    void trainingList() {
      //given
      List<BasicTraining> beforeList = basicTrainingRepository.findAll();

      basicTrainingRepository.save(BasicTraining.builder()
          .trainingName("test")
          .trainingNameKor("테스트")
          .bodyPart(BodyPart.ETC)
          .build());
      basicTrainingRepository.save(BasicTraining.builder()
          .trainingName("test1")
          .trainingNameKor("테스트1")
          .bodyPart(BodyPart.ETC)
          .build());

      //when
      List<BasicTrainingDto> list = adminTrainingService.trainingList();

      //then
      assertThat(list.size()).isEqualTo(beforeList.size() + 2);
    }
  }


}