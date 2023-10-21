package com.example.fitnessrecord.domain.userbodyinfo.service;

import static org.assertj.core.api.Assertions.*;

import com.example.fitnessrecord.domain.user.persist.User;
import com.example.fitnessrecord.domain.user.persist.UserRepository;
import com.example.fitnessrecord.domain.userbodyinfo.dto.BodyInfoDto;
import com.example.fitnessrecord.domain.userbodyinfo.dto.BodyInfoInput;
import com.example.fitnessrecord.global.exception.ErrorCode;
import com.example.fitnessrecord.global.exception.MyException;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
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
  UserRepository userRepository;

  @Nested
  @DisplayName("BODY_INFO 등록")
  class AddBodyInfo{
    @Test
    @DisplayName("성공")
    void addBodyInfo() {
      //given
      User user = User.builder()
          .email("test@test.com")
          .build();
      User saved = userRepository.save(user);

      BodyInfoInput input = BodyInfoInput.builder()
          .height(170)
          .weight(70.5)
          .muscleMass(30)
          .fatMass(21.5)
          .build();

      //when
      BodyInfoDto result = bodyInfoService.addBodyInfo(saved.getId(), input);

      //then
      assertThat(saved.getId()).isEqualTo(result.getUserId());
      assertThat(result.getHeight()).isEqualTo(input.getHeight());
      assertThat(result.getWeight()).isEqualTo(input.getWeight());
    }

    @Test
    @DisplayName("실패 : 유저가 존재하지 않음")
    void addBodyInfo_USER_NOT_FOUND() {
      //given
      User user = User.builder()
          .email("test@test.com")
          .build();

      BodyInfoInput input = BodyInfoInput.builder()
          .height(170)
          .weight(70.5)
          .muscleMass(30)
          .fatMass(21.5)
          .build();
      try{
        bodyInfoService.addBodyInfo(-1L, input);
      }catch(MyException e){
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
      }
    }

  }

}