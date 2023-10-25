package com.example.fitnessrecord.domain.training.custom.service;


import com.example.fitnessrecord.domain.training.custom.dto.AddCustomTrainingInput;
import com.example.fitnessrecord.domain.training.custom.dto.CustomTrainingDto;
import com.example.fitnessrecord.domain.training.custom.dto.EditCustomTrainingInput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomTrainingServiceImpl implements CustomTrainingService{

  @Override
  public CustomTrainingDto addCustomTraining(String userId, AddCustomTrainingInput input) {
    return null;
  }

  @Override
  public CustomTrainingDto editCustomTraining(String userId, EditCustomTrainingInput input) {
    return null;
  }

  @Override
  public CustomTrainingDto deleteCustomTraining(String userId, String trainingName) {
    return null;
  }

  @Override
  public Page<CustomTrainingDto> customTrainingList(String userId) {
    return null;
  }
}
