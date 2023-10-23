package com.example.fitnessrecord.domain.training.basic.service;

import com.example.fitnessrecord.domain.training.basic.dto.BasicTrainingDto;
import com.example.fitnessrecord.domain.training.basic.dto.AddBasicTrainingInput;
import com.example.fitnessrecord.domain.training.basic.dto.EditBasicTrainingInput;
import com.example.fitnessrecord.domain.training.basic.persist.BasicTraining;
import com.example.fitnessrecord.domain.training.basic.persist.BasicTrainingRepository;
import com.example.fitnessrecord.global.exception.ErrorCode;
import com.example.fitnessrecord.global.exception.MyException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AdminTrainingService {

  private final BasicTrainingRepository basicTrainingRepository;

  /**
   * ADMIN - Basic Training 추가
   */
  public BasicTrainingDto addTraining(AddBasicTrainingInput input){
    if(basicTrainingRepository.existsByTrainingName(input.getTrainingName())){
      throw new MyException(ErrorCode.TRAINING_NAME_ALREADY_EXIST);
    }

    BasicTraining basicTraining = BasicTraining.builder()
        .trainingName(input.getTrainingName())
        .trainingNameKor(input.getTrainingNameKor())
        .bodyPart(input.getBodyPart())
        .build();

    BasicTraining saved = basicTrainingRepository.save(basicTraining);

    return BasicTrainingDto.fromEntity(saved);

  }

  /**
   * ADMIN - Basic Training 삭제
   */
  public BasicTrainingDto deleteTraining(String name) {
    BasicTraining basicTraining = basicTrainingRepository.findByTrainingName(name)
        .orElseThrow(() -> new MyException(ErrorCode.TRAINING_NAME_NOT_FOUND));

    basicTrainingRepository.delete(basicTraining);

    return BasicTrainingDto.fromEntity(basicTraining);
  }

  /**
   * ADMIN - Basic Training 리스트
   */
  public List<BasicTrainingDto> list() {
    List<BasicTraining> list = basicTrainingRepository.findAll();
    return list.stream().map(t -> BasicTrainingDto.fromEntity(t)).collect(Collectors.toList());
  }

  /**
   * ADMIN - Basic Training 수정
   */
  public BasicTrainingDto updateTraining(EditBasicTrainingInput input) {
    BasicTraining basicTraining = basicTrainingRepository.findById(input.getId())
        .orElseThrow(() -> new MyException(ErrorCode.TRAINING_NAME_NOT_FOUND));
    basicTraining.setTrainingName(input.getTrainingName());
    basicTraining.setTrainingNameKor(input.getTrainingNameKor());
    basicTraining.setBodyPart(input.getBodyPart());
    basicTrainingRepository.save(basicTraining);

    return BasicTrainingDto.fromEntity(basicTraining);
  }
}
