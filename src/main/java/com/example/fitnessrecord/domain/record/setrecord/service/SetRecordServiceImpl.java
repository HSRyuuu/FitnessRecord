package com.example.fitnessrecord.domain.record.setrecord.service;

import com.example.fitnessrecord.domain.record.setrecord.dto.AddSetRecordResult;
import com.example.fitnessrecord.domain.record.setrecord.dto.DeleteSetRecordResult;
import com.example.fitnessrecord.domain.record.setrecord.dto.SetRecordInput;
import com.example.fitnessrecord.domain.record.setrecord.persist.SetRecord;
import com.example.fitnessrecord.domain.record.setrecord.persist.SetRecordRepository;
import com.example.fitnessrecord.domain.record.trainingrecord.dto.TrainingRecordDto;
import com.example.fitnessrecord.domain.record.trainingrecord.persist.TrainingRecord;
import com.example.fitnessrecord.domain.record.trainingrecord.persist.TrainingRecordRepository;
import com.example.fitnessrecord.global.exception.ErrorCode;
import com.example.fitnessrecord.global.exception.MyException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class SetRecordServiceImpl implements SetRecordService {

  private final SetRecordRepository setRecordRepository;
  private final TrainingRecordRepository trainingRecordRepository;

  @Override
  public AddSetRecordResult addSetRecords(Long trainingRecordId, List<SetRecordInput> list) {
    TrainingRecord trainingRecord = trainingRecordRepository.findById(trainingRecordId)
        .orElseThrow(() -> new MyException(ErrorCode.TRAINING_RECORD_NOT_FOUND));

    List<SetRecord> setRecords = setRecordRepository.saveAll(
        list.stream()
            .map(x -> SetRecordInput.toEntity(x, trainingRecord))
            .collect(Collectors.toList())
    );

    this.saveLastModifiedDateOfTrainingRecord(trainingRecord);

    return new AddSetRecordResult(TrainingRecordDto.fromEntity(trainingRecord), setRecords.size());
  }

  @Override
  public DeleteSetRecordResult deleteSetRecord(Long id, Long userId) {
    SetRecord setRecord = setRecordRepository.findById(id)
        .orElseThrow(() -> new MyException(ErrorCode.SET_RECORD_NOT_FOUND));

    if(!Objects.equals(setRecord.getUser().getId(), userId)){
      throw new MyException(ErrorCode.NO_AUTHORITY_ERROR);
    }

    //set record 수정 시 TrainingRecord 수정
    this.saveLastModifiedDateOfTrainingRecord(setRecord.getTrainingRecord());

    setRecordRepository.delete(setRecord);

    return DeleteSetRecordResult.fromEntity(setRecord);
  }

  private void saveLastModifiedDateOfTrainingRecord(TrainingRecord trainingRecord){
    trainingRecord.setLastModifiedDate(LocalDate.now());
    trainingRecordRepository.save(trainingRecord);
  }
}
