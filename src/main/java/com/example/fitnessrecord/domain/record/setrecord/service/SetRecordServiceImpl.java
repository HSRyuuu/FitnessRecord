package com.example.fitnessrecord.domain.record.setrecord.service;

import com.example.fitnessrecord.domain.record.setrecord.dto.AddSetRecordResult;
import com.example.fitnessrecord.domain.record.setrecord.dto.DeleteSetRecordResult;
import com.example.fitnessrecord.domain.record.setrecord.dto.SetRecordDto;
import com.example.fitnessrecord.domain.record.setrecord.dto.SetRecordInput;
import com.example.fitnessrecord.domain.record.setrecord.persist.SetRecord;
import com.example.fitnessrecord.domain.record.setrecord.persist.SetRecordRepository;
import com.example.fitnessrecord.domain.record.trainingrecord.dto.TrainingRecordDto;
import com.example.fitnessrecord.domain.record.trainingrecord.persist.TrainingRecord;
import com.example.fitnessrecord.domain.record.trainingrecord.persist.TrainingRecordRepository;
import com.example.fitnessrecord.domain.record.volume.service.VolumeRecordService;
import com.example.fitnessrecord.global.exception.ErrorCode;
import com.example.fitnessrecord.global.exception.MyException;
import com.example.fitnessrecord.global.redis.xxx.DistributedLock;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class SetRecordServiceImpl implements SetRecordService {

  private final SetRecordRepository setRecordRepository;
  private final TrainingRecordRepository trainingRecordRepository;
  private final VolumeRecordService volumeRecordService;
  private final RedissonClient redissonClient;


  @Override
  @Transactional
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
  @Transactional
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
  @Transactional
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
  @Transactional
  public SetRecordDto updateSetRecord(Long id, Long userId, SetRecordInput input) {

    SetRecord setRecord = setRecordRepository.findById(id)
        .orElseThrow(() -> new MyException(ErrorCode.SET_RECORD_NOT_FOUND));
    //user 1 : (1) -> (2)   (1) -> (3)

    this.authorityValidation(userId, setRecord);
    String lockName = "SetRecord" + setRecord.getId();

    SetRecord saved = lockExample(lockName, SetRecordInput.updateSetRecord(setRecord, input));

    return SetRecordDto.fromEntity(saved);
  }

  private SetRecord lockExample(String lockName, SetRecord setRecord){

    RLock rLock = redissonClient.getLock(lockName);
    log.info("RLock: {}", rLock);

    long waitTime = 2L;
    long leaseTime = 5L;
    TimeUnit timeUnit = TimeUnit.SECONDS; //시간 단위 = 초
    try{
      boolean available =
          rLock.tryLock(waitTime, leaseTime, timeUnit);
      if(!available){
        throw new MyException(ErrorCode.REDIS_LOCK);
      }

      return updateSetRecordAndVolumeRecord(setRecord);
    }catch (InterruptedException e) {
      log.info("=== InterruptedException ===");
      throw new MyException(ErrorCode.REDIS_LOCK);
    }catch (Exception e){
      e.printStackTrace();
    }finally {
      log.info("=== finally ===");
      try{
        rLock.unlock();
        log.info("=== unlock complete ===");
      }catch (IllegalMonitorStateException e){
        e.printStackTrace();
      }
    }
    return null;
  }

  private SetRecord updateSetRecordAndVolumeRecord(SetRecord setRecord)
      throws InterruptedException {
    log.info("updateSetRecordAndVolumeRecord");
    //SetRecord 업데이트
    SetRecord saved = setRecordRepository.save(setRecord);

    TrainingRecord trainingRecord = setRecord.getTrainingRecord();

    //VolumeRecord update
    if(!trainingRecord.getDate().isEqual(LocalDate.now())){
      volumeRecordService.updateVolumeRecord(trainingRecord);
      this.saveLastModifiedDateOfTrainingRecord(trainingRecord);
    }
    Thread.sleep(3000L);
    return saved;
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
