package com.example.fitnessrecord.domain.training.custom.service;


import com.example.fitnessrecord.domain.training.custom.dto.AddCustomTrainingInput;
import com.example.fitnessrecord.domain.training.custom.dto.CustomTrainingDto;
import com.example.fitnessrecord.domain.training.custom.dto.EditCustomTrainingInput;
import org.springframework.data.domain.Page;

public interface CustomTrainingService {

  /**
   * Custom 운동 추가
   */
  CustomTrainingDto addCustomTraining(String userEmail, AddCustomTrainingInput input);

  /**
   * Custom 운동 수정
   */
  CustomTrainingDto editCustomTraining(String userEmail, EditCustomTrainingInput input);

  /**
   * Custom 운동 삭제
   */
  CustomTrainingDto deleteCustomTraining(String userEmail, String trainingName);

  /**
   * Custom 운동 리스트
   */
  Page<CustomTrainingDto> customTrainingList(String userEmail);

}
