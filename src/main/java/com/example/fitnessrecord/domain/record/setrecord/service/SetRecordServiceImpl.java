package com.example.fitnessrecord.domain.record.setrecord.service;

import com.example.fitnessrecord.domain.record.setrecord.dto.AddSetRecordResult;
import com.example.fitnessrecord.domain.record.setrecord.dto.DeleteSetRecordResult;
import com.example.fitnessrecord.domain.record.setrecord.dto.SetRecordDto;
import com.example.fitnessrecord.domain.record.setrecord.dto.SetRecordInput;
import com.example.fitnessrecord.domain.record.setrecord.dto.SetRecordUpdateDto;
import com.example.fitnessrecord.domain.record.setrecord.persist.SetRecord;
import com.example.fitnessrecord.domain.record.setrecord.persist.SetRecordRepository;
import com.example.fitnessrecord.domain.record.trainingrecord.dto.TrainingRecordDto;
import com.example.fitnessrecord.domain.record.trainingrecord.persist.TrainingRecord;
import com.example.fitnessrecord.domain.record.trainingrecord.persist.TrainingRecordRepository;
import com.example.fitnessrecord.domain.record.volume.service.VolumeRecordService;
import com.example.fitnessrecord.global.exception.ErrorCode;
import com.example.fitnessrecord.global.exception.MyException;
import java.time.LocalDate;
import java.util.List;
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
  private final VolumeRecordService volumeRecordService;

  @Override
  public AddSetRecordResult addSetRecords(Long trainingRecordId, List<SetRecordInput> list) {
    TrainingRecord trainingRecord = trainingRecordRepository.findById(trainingRecordId)
        .orElseThrow(() -> new MyException(ErrorCode.TRAINING_RECORD_NOT_FOUND));

    List<SetRecord> setRecords = setRecordRepository.saveAll(
        list.stream()
            .map(x -> SetRecordInput.toEntity(x, trainingRecord))
            .collect(Collectors.toList())
    );

    return new AddSetRecordResult(TrainingRecordDto.fromEntity(trainingRecord), setRecords.size());
  }

  @Override
  public SetRecordDto addSetRecord(Long trainingRecordId, SetRecordInput input) {
    TrainingRecord trainingRecord = trainingRecordRepository.findById(trainingRecordId)
        .orElseThrow(() -> new MyException(ErrorCode.TRAINING_RECORD_NOT_FOUND));

    SetRecord setRecord = SetRecordInput.toEntity(input, trainingRecord);
    SetRecord saved = setRecordRepository.save(setRecord);

    //오늘 만들어진 운동이 아니면 Volume 기록도 수정
    if (!trainingRecord.getDate().isEqual(LocalDate.now())) {
      volumeRecordService.updateVolumeRecord(trainingRecord);
      this.saveLastModifiedDateOfTrainingRecord(trainingRecord);
    }

    return SetRecordDto.fromEntity(saved);
  }

  @Override
  public DeleteSetRecordResult deleteSetRecord(Long id, Long userId) {
    SetRecord setRecord = setRecordRepository.findById(id)
        .orElseThrow(() -> new MyException(ErrorCode.SET_RECORD_NOT_FOUND));

    this.authorityValidation(userId, setRecord);

    TrainingRecord trainingRecord = setRecord.getTrainingRecord();

    setRecordRepository.delete(setRecord);

    //오늘 만들어진 운동이 아니면 Volume 기록도 수정
    if (!trainingRecord.getDate().isEqual(LocalDate.now())) {
      volumeRecordService.updateVolumeRecord(trainingRecord);
      this.saveLastModifiedDateOfTrainingRecord(trainingRecord);
    }

    return DeleteSetRecordResult.fromEntity(setRecord);
  }

  @Override
  public SetRecordDto updateSetRecord(Long id, Long userId, SetRecordInput input) {

    SetRecord setRecord = setRecordRepository.findById(id)
        .orElseThrow(() -> new MyException(ErrorCode.SET_RECORD_NOT_FOUND));

    this.authorityValidation(userId, setRecord);

    SetRecordUpdateDto updateDto =
        new SetRecordUpdateDto(setRecord.getBodyPart(), setRecord.getWeight() * setRecord.getReps());

    SetRecord saved = setRecordRepository.save(SetRecordInput.updateSetRecord(setRecord, input));
    updateDto.setBodyPartAfter(saved.getBodyPart());
    updateDto.setVolumeAfter(saved.getWeight() * saved.getReps());

    TrainingRecord trainingRecord = setRecord.getTrainingRecord();

    if(!trainingRecord.getDate().isEqual(LocalDate.now())){
      volumeRecordService.updateVolumeRecord(trainingRecord);
      this.saveLastModifiedDateOfTrainingRecord(trainingRecord);
    }

    return SetRecordDto.fromEntity(saved);
  }


  private void authorityValidation(Long userId, SetRecord setRecord) {
    if(!setRecord.getUser().getId().equals(userId)){
      throw new MyException(ErrorCode.NO_AUTHORITY_ERROR);
    }
  }

  private void saveLastModifiedDateOfTrainingRecord(TrainingRecord trainingRecord) {
    trainingRecord.setLastModifiedDate(LocalDate.now());
    trainingRecordRepository.save(trainingRecord);
  }

}
