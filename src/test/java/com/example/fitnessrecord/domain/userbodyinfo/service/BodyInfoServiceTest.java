package com.example.fitnessrecord.domain.userbodyinfo.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.fitnessrecord.domain.user.persist.User;
import com.example.fitnessrecord.domain.user.persist.UserRepository;
import com.example.fitnessrecord.domain.userbodyinfo.dto.BodyInfoDto;
import com.example.fitnessrecord.domain.userbodyinfo.dto.BodyInfoInput;
import com.example.fitnessrecord.domain.userbodyinfo.persist.BodyInfo;
import com.example.fitnessrecord.domain.userbodyinfo.persist.BodyInfoRepository;
import com.example.fitnessrecord.global.exception.ErrorCode;
import com.example.fitnessrecord.global.exception.MyException;
import java.time.LocalDate;
import java.util.List;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@Transactional
@SpringBootTest
class BodyInfoServiceTest {

  @Autowired
  BodyInfoService bodyInfoService;
  @Autowired
  BodyInfoRepository bodyInfoRepository;
  @Autowired
  UserRepository userRepository;

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
  @DisplayName("바디 데이터 등록(추가)")
  class AddBodyInfo {

    @Test
    @DisplayName("성공")
    void addBodyInfo() {
      //given
      BodyInfoInput input = BodyInfoInput.builder()
          .height(170)
          .weight(70.5)
          .muscleMass(30)
          .fatMass(21.5)
          .build();

      //when
      BodyInfoDto result = bodyInfoService.addBodyInfo(user.getId(), input);

      //then
      assertThat(user.getId()).isEqualTo(result.getUserId());
      assertThat(result.getHeight()).isEqualTo(input.getHeight());
      assertThat(result.getWeight()).isEqualTo(input.getWeight());
    }

    @Test
    @DisplayName("성공 - 하루에 여러번 추가")
    void addBodyInfoTwice() {
      //given
      BodyInfoInput input1 = BodyInfoInput.builder()
          .height(170)
          .weight(70.5)
          .muscleMass(30)
          .fatMass(21.5)
          .build();
      bodyInfoService.addBodyInfo(user.getId(), input1);

      BodyInfoInput input2 = BodyInfoInput.builder()
          .height(170)
          .weight(75)
          .muscleMass(35)
          .fatMass(20)
          .build();

      //when
      bodyInfoService.addBodyInfo(user.getId(), input2);

      BodyInfo bodyInfo = bodyInfoRepository.findByUserAndDate(user, LocalDate.now()).get();

      //then
      assertThat(bodyInfo.getMuscleMass()).isEqualTo(input2.getMuscleMass());
      assertThat(bodyInfo.getFatMass()).isEqualTo(input2.getFatMass());
    }

    @Test
    @DisplayName("실패 : 유저가 존재하지 않음")
    void addBodyInfo_USER_NOT_FOUND() {
      //given
      BodyInfoInput input = BodyInfoInput.builder()
          .height(170)
          .weight(70.5)
          .muscleMass(30)
          .fatMass(21.5)
          .build();
      try {
        bodyInfoService.addBodyInfo(-1L, input);
      } catch (MyException e) {
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
      }
    }
  }

  @Nested
  @DisplayName("바디 데이터 삭제")
  class DeleteByDate {

    @Test
    @DisplayName("성공")
    void deleteByDate() {
      //given
      BodyInfo bodyInfoInput = BodyInfo.builder()
          .user(user)
          .height(170)
          .weight(70)
          .muscleMass(30)
          .fatMass(10)
          .date(LocalDate.of(2010, 10, 10))
          .build();
      BodyInfo bodyInfo = bodyInfoRepository.save(bodyInfoInput);

      //when
      bodyInfoService.deleteByDate(user.getId(), LocalDate.of(2010, 10, 10));

      //then
      assertThat(bodyInfoRepository.findById(bodyInfo.getId()).isEmpty()).isTrue();
    }

    @Test
    @DisplayName("실패 : 해당 일자에 바디 데이터가 존재하지 않음")
    void deleteByDate_BODY_INFO_NOT_FOUND() {
      //given
      BodyInfo bodyInfoInput = BodyInfo.builder()
          .user(user)
          .height(170)
          .weight(70)
          .muscleMass(30)
          .fatMass(10)
          .date(LocalDate.of(2010, 10, 10))
          .build();
      BodyInfo bodyInfo = bodyInfoRepository.save(bodyInfoInput);

      //when
      //then
      try {
        bodyInfoService.deleteByDate(user.getId(), LocalDate.of(2010, 9, 9));
      } catch (MyException e) {
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.BODY_INFO_DATA_NOT_FOUND);
      }
    }
  }

  @Nested
  @DisplayName("바디 데이터 리스트 기간 별 찾기")
  class BodyInfoListByPeriod {

    @BeforeEach
    void addTwoBodyInfo() {
      BodyInfo bodyInfoInput1 = BodyInfo.builder()
          .user(user)
          .height(170)
          .weight(70)
          .muscleMass(30)
          .fatMass(10)
          .date(LocalDate.of(2010, 10, 9))
          .build();
      BodyInfo bodyInfoInput2 = BodyInfo.builder()
          .user(user)
          .height(170)
          .weight(70)
          .muscleMass(30)
          .fatMass(10)
          .date(LocalDate.of(2010, 10, 13))
          .build();
      bodyInfoRepository.save(bodyInfoInput1);
      bodyInfoRepository.save(bodyInfoInput2);
    }

    @Test
    @DisplayName("성공 - 두개 조회됨")
    void bodyInfoListByPeriod_find_two() {
      //given
      //addTwoBodyInfo()
      LocalDate start = LocalDate.of(2010, 10, 1);
      LocalDate end = LocalDate.of(2010, 10, 15);

      //when
      List<BodyInfoDto> findList =
          bodyInfoService.bodyInfoListByPeriod(user.getId(), start, end);

      //then
      assertThat(findList.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("성공 - 하나만 조회됨")
    void bodyInfoListByPeriod_find_one() {
      //given
      //addTwoBodyInfo()
      LocalDate start = LocalDate.of(2010, 10, 1);
      LocalDate end = LocalDate.of(2010, 10, 9);

      //when
      List<BodyInfoDto> findList =
          bodyInfoService.bodyInfoListByPeriod(user.getId(), start, end);

      //then
      assertThat(findList.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("실패: 해당 기간에 조회 결과 없음")
    void bodyInfoListByPeriod_BODY_INFO_DATA_NOT_FOUND() {
      //given
      //addTwoBodyInfo()
      LocalDate start = LocalDate.of(2010, 10, 1);
      LocalDate end = LocalDate.of(2010, 10, 5);

      //when
      //then
      try{
        bodyInfoService.bodyInfoListByPeriod(user.getId(), start, end);
      }catch(MyException e){
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.BODY_INFO_DATA_NOT_FOUND);
      }
    }

  }

}
