package com.example.fitnessrecord.domain.training.custom.service;


import com.example.fitnessrecord.domain.training.custom.dto.AddCustomTrainingInput;
import com.example.fitnessrecord.domain.training.custom.dto.CustomTrainingDto;
import com.example.fitnessrecord.domain.training.custom.dto.EditCustomTrainingInput;
import com.example.fitnessrecord.domain.training.custom.persist.CustomTraining;
import com.example.fitnessrecord.domain.training.custom.persist.CustomTrainingRepository;
import com.example.fitnessrecord.domain.user.persist.User;
import com.example.fitnessrecord.domain.user.persist.UserRepository;
import com.example.fitnessrecord.global.exception.ErrorCode;
import com.example.fitnessrecord.global.exception.MyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomTrainingServiceImpl implements CustomTrainingService{

  private final UserRepository userRepository;
  private final CustomTrainingRepository customTrainingRepository;

  @Override
  public CustomTrainingDto addCustomTraining(String userEmail, AddCustomTrainingInput input) {

    User user = userRepository.findByEmail(userEmail)
        .orElseThrow(() -> new MyException(ErrorCode.USER_NOT_FOUND));

    CustomTraining customTraining = AddCustomTrainingInput.toEntity(user, input);

    CustomTraining saved = customTrainingRepository.save(customTraining);

    return CustomTrainingDto.fromEntity(saved);
  }

  @Override
  public CustomTrainingDto editCustomTraining(String userEmail, EditCustomTrainingInput input) {
    return null;
  }

  @Override
  public CustomTrainingDto deleteCustomTraining(String userEmail, String trainingName) {
    return null;
  }

  @Override
  public Page<CustomTrainingDto> customTrainingList(String userEmail) {
    return null;
  }
}
