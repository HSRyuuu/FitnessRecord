package com.example.fitnessrecord.domain.record.trainingrecord.service;

import com.example.fitnessrecord.domain.record.setrecord.dto.SetRecordDto;
import com.example.fitnessrecord.domain.record.setrecord.persist.SetRecord;
import com.example.fitnessrecord.domain.record.setrecord.persist.SetRecordRepository;
import com.example.fitnessrecord.domain.record.trainingrecord.dto.TrainingRecordDto;
import com.example.fitnessrecord.domain.record.trainingrecord.dto.TrainingRecordResponse;
import com.example.fitnessrecord.domain.record.trainingrecord.persist.TrainingRecord;
import com.example.fitnessrecord.domain.record.trainingrecord.persist.TrainingRecordRepository;
import com.example.fitnessrecord.domain.user.persist.User;
import com.example.fitnessrecord.domain.user.persist.UserRepository;
import com.example.fitnessrecord.global.exception.ErrorCode;
import com.example.fitnessrecord.global.exception.MyException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TrainingRecordServiceImpl implements TrainingRecordService {

  private final TrainingRecordRepository trainingRecordRepository;
  private final SetRecordRepository setRecordRepository;
  private final UserRepository userRepository;

  @Override
  public TrainingRecordDto addTrainingRecord(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new MyException(ErrorCode.USER_NOT_FOUND));

    TrainingRecord saved =
        trainingRecordRepository.save(TrainingRecord.builder()
            .user(user)
            .date(LocalDate.now())
            .build()
        );

    return TrainingRecordDto.fromEntity(saved);
  }


  @Override
  public TrainingRecordResponse getTrainingRecordInfo(Long trainingRecordId, String username) {
    TrainingRecord trainingRecord = trainingRecordRepository.findById(trainingRecordId)
        .orElseThrow(() -> new MyException(ErrorCode.TRAINING_RECORD_NOT_FOUND));

    if(!trainingRecord.getUser().getEmail().equals(username)){
      throw new MyException(ErrorCode.NO_AUTHORITY_ERROR);
    }

    List<SetRecord> list = setRecordRepository.findAllByTrainingRecordId(
        trainingRecordId);

    return new TrainingRecordResponse(
        TrainingRecordDto.fromEntity(trainingRecord),
        list.stream().map(SetRecordDto::fromEntity).collect(Collectors.toList())
    );

  }
}
