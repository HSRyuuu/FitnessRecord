package com.example.fitnessrecord.domain.record.trainingrecord.service;

import com.example.fitnessrecord.domain.record.trainingrecord.dto.TrainingRecordDto;
import com.example.fitnessrecord.domain.record.trainingrecord.persist.TrainingRecord;
import com.example.fitnessrecord.domain.record.trainingrecord.persist.TrainingRecordRepository;
import com.example.fitnessrecord.domain.user.persist.User;
import com.example.fitnessrecord.domain.user.persist.UserRepository;
import com.example.fitnessrecord.global.exception.ErrorCode;
import com.example.fitnessrecord.global.exception.MyException;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TrainingRecordServiceImpl implements TrainingRecordService {

  private final TrainingRecordRepository trainingRecordRepository;
  private final UserRepository userRepository;

  @Override
  public TrainingRecordDto addRecord(Long userId) {
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
}
