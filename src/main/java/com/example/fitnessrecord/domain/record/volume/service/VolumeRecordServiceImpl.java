package com.example.fitnessrecord.domain.record.volume.service;

import com.example.fitnessrecord.domain.record.setrecord.persist.SetRecord;
import com.example.fitnessrecord.domain.record.setrecord.persist.SetRecordRepository;
import com.example.fitnessrecord.domain.record.trainingrecord.persist.TrainingRecord;
import com.example.fitnessrecord.domain.record.trainingrecord.persist.TrainingRecordRepository;
import com.example.fitnessrecord.domain.record.volume.persist.VolumeRecord;
import com.example.fitnessrecord.domain.record.volume.persist.VolumeRecordRepository;
import com.example.fitnessrecord.domain.record.volume.util.VolumeRecordUtils;
import com.example.fitnessrecord.domain.training.common.type.BodyPart;
import com.example.fitnessrecord.global.exception.ErrorCode;
import com.example.fitnessrecord.global.exception.MyException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class VolumeRecordServiceImpl implements VolumeRecordService {

  private final SetRecordRepository setRecordRepository;
  private final TrainingRecordRepository trainingRecordRepository;
  private final VolumeRecordRepository volumeRecordRepository;

  @Override
  public int executeVolumeRecordProcess(List<TrainingRecord> trainingRecords) {
    for (TrainingRecord trainingRecord : trainingRecords) {
      //이미 volumeRecord가 존재할 때 (이전에 저장 후 수정되었을 때)
      if (volumeRecordRepository.existsByTrainingRecord(trainingRecord)) {
        this.updateVolumeRecord(trainingRecord);
      }//처음 저장할 때
      else {
        this.saveVolumeRecord(trainingRecord);
      }
    }
    return trainingRecords.size();
  }

  @Override
  public boolean updateVolumeRecordByTrainingRecordId(Long trainingRecordId) {
    TrainingRecord trainingRecord = trainingRecordRepository.findById(trainingRecordId)
        .orElseThrow(() -> new MyException(ErrorCode.TRAINING_RECORD_NOT_FOUND));

    //오늘 생성한 trainingRecord를 수정 또는 삭제한 경우에는 update하지 않음
    if (!trainingRecord.getDate().isBefore(LocalDate.now())) {
      return false;
    }

    this.updateVolumeRecord(trainingRecord);
    return true;
  }

  private void updateVolumeRecord(TrainingRecord trainingRecord) {
    VolumeRecord volumeRecord = volumeRecordRepository.findByTrainingRecord(trainingRecord)
        .orElseThrow(() -> new MyException(ErrorCode.VOLUME_RECORD_NOT_FOUND));

    //TrainingRecord로부터 SetRecordList 찾기
    List<SetRecord> setRecords = setRecordRepository.findByTrainingRecord(trainingRecord);

    Map<BodyPart, Double> volumeMap = VolumeRecordUtils.getVolumeMapBySetRecords(setRecords);

    //기존 volumeRecord에 새로 생성한 volumeMap 값 업데이트 후 저장
    VolumeRecord saved =
        volumeRecordRepository.save(
            VolumeRecordUtils.setVolumeRecordFieldsFromMap(volumeRecord, volumeMap));

    log.info("volume record saved: {}", saved);

  }

  /**
   * TrainingRecord의 volume을 새로 저장
   */
  private void saveVolumeRecord(TrainingRecord trainingRecord) {
    List<SetRecord> setRecords = setRecordRepository.findByTrainingRecord(trainingRecord);

    Map<BodyPart, Double> volumeMap = VolumeRecordUtils.getVolumeMapBySetRecords(setRecords);

    VolumeRecord volumeRecord =
        VolumeRecordUtils.setVolumeRecordFieldsFromMap(new VolumeRecord(), volumeMap);

    //TrainingRecord로부터 나머지 필드들 세팅
    this.setRemainingElementsFromTrainingRecord(volumeRecord, trainingRecord);

    VolumeRecord saved = volumeRecordRepository.save(volumeRecord);

    log.info("volume record saved: {}", saved);
  }

  /**
   * Volume에 대한 필드 이외에 나머지 필드 세팅
   */
  private void setRemainingElementsFromTrainingRecord(
      VolumeRecord volumeRecord, TrainingRecord trainingRecord) {
    volumeRecord.setTrainingRecord(trainingRecord);
    volumeRecord.setUser(trainingRecord.getUser());
    volumeRecord.setDate(trainingRecord.getDate());
  }

}
