package com.example.fitnessrecord.domain.record.volume.service;

import com.example.fitnessrecord.domain.record.setrecord.persist.SetRecord;
import com.example.fitnessrecord.domain.record.setrecord.persist.SetRecordRepository;
import com.example.fitnessrecord.domain.record.trainingrecord.persist.TrainingRecord;
import com.example.fitnessrecord.domain.record.trainingrecord.persist.TrainingRecordRepository;
import com.example.fitnessrecord.domain.record.volume.persist.VolumeRecord;
import com.example.fitnessrecord.domain.record.volume.persist.VolumeRecordRepository;
import com.example.fitnessrecord.domain.training.common.type.BodyPart;
import com.example.fitnessrecord.global.exception.ErrorCode;
import com.example.fitnessrecord.global.exception.MyException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
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

  public boolean updateVolumeRecord(Long trainingRecordId) {
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
    Optional<VolumeRecord> optionalVolumeRecord =
        volumeRecordRepository.findByTrainingRecord(trainingRecord);
    if (optionalVolumeRecord.isEmpty()) {
      return;
    }
    //기존에 존재하는 volumeRecord 찾기
    VolumeRecord volumeRecord = optionalVolumeRecord.get();

    //TrainingRecord로부터 SetRecordList 찾기
    List<SetRecord> setRecords = setRecordRepository.findByTrainingRecord(trainingRecord);

    Map<BodyPart, Double> volumeMap = this.getVolumeMapBySetRecords(setRecords);

    //기존 volumeRecord에 새로 생성한 volumeMap 저장
    this.updateVolumeRecordByVolumeMap(volumeRecord, volumeMap);

    VolumeRecord saved = volumeRecordRepository.save(volumeRecord);

    log.info("volume record saved: {}", saved);

    //trainingRecord의 volume 저장 여부를 true로 변경
    trainingRecord.setVolumeSavedYn(true);
    trainingRecordRepository.save(trainingRecord);
  }

  /**
   * 전달받은 volumeMap의 volume 값들을 엔티티에 세팅
   */
  private void updateVolumeRecordByVolumeMap(VolumeRecord volumeRecord,
      Map<BodyPart, Double> volumeMap) {
    volumeRecord.setChest(volumeMap.getOrDefault(BodyPart.CHEST, 0.0));
    volumeRecord.setBack(volumeMap.getOrDefault(BodyPart.BACK, 0.0));
    volumeRecord.setLegs(volumeMap.getOrDefault(BodyPart.LEGS, 0.0));
    volumeRecord.setShoulder(volumeMap.getOrDefault(BodyPart.SHOULDER, 0.0));
    volumeRecord.setBiceps(volumeMap.getOrDefault(BodyPart.BICEPS, 0.0));
    volumeRecord.setTriceps(volumeMap.getOrDefault(BodyPart.TRICEPS, 0.0));
    volumeRecord.setEtc(volumeMap.getOrDefault(BodyPart.ETC, 0.0));
  }

  /**
   * TrainingRecord의 volume을 새로 저장
   */
  private void saveVolumeRecord(TrainingRecord trainingRecord) {
    List<SetRecord> setRecords = setRecordRepository.findByTrainingRecord(trainingRecord);

    Map<BodyPart, Double> volumeMap = this.getVolumeMapBySetRecords(setRecords);

    VolumeRecord volumeRecord = this.createVolumeRecord(volumeMap);

    //TrainingRecord로부터 나머지 필드들 세팅
    this.setRemainingElementsFromTrainingRecord(volumeRecord, trainingRecord);

    VolumeRecord saved = volumeRecordRepository.save(volumeRecord);

    log.info("volume record saved: {}", saved);

    trainingRecord.setVolumeSavedYn(true);
    trainingRecordRepository.save(trainingRecord);
  }

  /**
   * SetRecord list의 요소들을 volume으로 변환하여 Map에 저장 후 반환
   */
  private Map<BodyPart, Double> getVolumeMapBySetRecords(List<SetRecord> setRecords) {
    return setRecords.stream()
        .collect(Collectors.toMap(
            item -> item.getBodyPart(), //key Mapper
            item -> item.getReps() * item.getWeight(), //value Mapper
            (v1, v2) -> v1 + v2) //key가 이미 존재할 때 merge 방법
        );

  }

  /**
   * 전달받은 volumeMap의 volume값을 VolumeRecord를 생성하여 세팅
   */
  private VolumeRecord createVolumeRecord(Map<BodyPart, Double> volumeMap) {
    return VolumeRecord.builder()
        .chest(volumeMap.getOrDefault(BodyPart.CHEST, 0.0))
        .back(volumeMap.getOrDefault(BodyPart.BACK, 0.0))
        .legs(volumeMap.getOrDefault(BodyPart.LEGS, 0.0))
        .shoulder(volumeMap.getOrDefault(BodyPart.SHOULDER, 0.0))
        .biceps(volumeMap.getOrDefault(BodyPart.BICEPS, 0.0))
        .triceps(volumeMap.getOrDefault(BodyPart.TRICEPS, 0.0))
        .etc(volumeMap.getOrDefault(BodyPart.ETC, 0.0))
        .build();
  }

  private void setRemainingElementsFromTrainingRecord(
      VolumeRecord volumeRecord, TrainingRecord trainingRecord) {
    volumeRecord.setTrainingRecord(trainingRecord);
    volumeRecord.setUser(trainingRecord.getUser());
    volumeRecord.setDate(trainingRecord.getDate());
  }


}
