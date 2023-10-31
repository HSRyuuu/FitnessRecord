package com.example.fitnessrecord.domain.record.volume.scheduler;

import com.example.fitnessrecord.domain.record.setrecord.persist.SetRecord;
import com.example.fitnessrecord.domain.record.setrecord.persist.SetRecordRepository;
import com.example.fitnessrecord.domain.record.trainingrecord.persist.TrainingRecord;
import com.example.fitnessrecord.domain.record.trainingrecord.persist.TrainingRecordRepository;
import com.example.fitnessrecord.domain.record.volume.persist.VolumeRecord;
import com.example.fitnessrecord.domain.record.volume.persist.VolumeRecordRepository;
import com.example.fitnessrecord.domain.training.common.type.BodyPart;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class VolumeScheduler {

  private final TrainingRecordRepository trainingRecordRepository;
  private final VolumeRecordRepository volumeRecordRepository;
  private final SetRecordRepository setRecordRepository;

  /**
   * 오늘 날짜로부터 7일 이전의 데이터를 Volume Record로 저장
   */
  @Scheduled(cron = "0 0 2 * * *") //매일 오전 2시에 실행
  public void saveVolumes() {
    log.info("(volume recording start) TIME: {}", LocalDateTime.now());

    List<TrainingRecord> trainingRecords =
        trainingRecordRepository.findAllByDateAndVolumeSavedYnIsFalse(LocalDate.now().minusDays(7));

    if (trainingRecords.isEmpty()) {
      log.info("trainingRecords is Empty!");
      return;
    }

    for (TrainingRecord trainingRecord : trainingRecords) {

      if (volumeRecordRepository.existsByTrainingRecordId(trainingRecord.getId())) {
        continue;
      }

      List<SetRecord> setRecords = setRecordRepository.findByTrainingRecord(trainingRecord);

      Map<BodyPart, Double> volumeMap = this.getVolumeMapBySetRecords(setRecords);

      VolumeRecord volumeRecord = this.createVolumeRecord(volumeMap);
      VolumeRecord saved = this.saveVolumeRecord(trainingRecord, volumeRecord);

      log.info("volume record saved: {}", saved);

      trainingRecord.setVolumeSavedYn(true);
      trainingRecordRepository.save(trainingRecord);
    }

  }

  private Map<BodyPart, Double> getVolumeMapBySetRecords(List<SetRecord> setRecords) {
    Map<BodyPart, Double> weightMap = new HashMap<>();

    for (SetRecord setRecord : setRecords) {
      BodyPart bodyPart = setRecord.getBodyPart();
      weightMap.put(bodyPart,
          weightMap.getOrDefault(bodyPart, 0.0) + setRecord.getWeight() * setRecord.getReps());
    }
    return weightMap;
  }

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

  private VolumeRecord saveVolumeRecord(
      TrainingRecord trainingRecord, VolumeRecord volumeRecord) {
    volumeRecord.setTrainingRecord(trainingRecord);
    volumeRecord.setUser(trainingRecord.getUser());
    volumeRecord.setDate(trainingRecord.getDate());

    return volumeRecordRepository.save(volumeRecord);
  }
}
