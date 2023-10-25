package com.example.fitnessrecord.domain.training.custom.service;


import com.example.fitnessrecord.domain.training.custom.dto.AddCustomTrainingInput;
import com.example.fitnessrecord.domain.training.custom.dto.CustomTrainingDto;
import com.example.fitnessrecord.domain.training.custom.dto.EditCustomTrainingInput;
import org.springframework.data.domain.Page;

public interface CustomTrainingService {

  /**
   * Custom 운동 종목 추가
   */
  CustomTrainingDto addCustomTraining(String username, AddCustomTrainingInput input);

  /**
   * Custom 운동 종목 수정
   */
  CustomTrainingDto editCustomTraining(String username, EditCustomTrainingInput input);

  /**
   * Custom 운동 종목 삭제
   */
  CustomTrainingDto deleteCustomTraining(String username, Long trainingId);

  /**
   * Custom 운동 종목 리스트
   */
  Page<CustomTrainingDto> customTrainingList(String username);

}
