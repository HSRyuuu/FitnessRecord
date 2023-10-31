package com.example.fitnessrecord.domain.record.volume.scheduler;

import com.example.fitnessrecord.domain.record.setrecord.persist.SetRecord;
import com.example.fitnessrecord.domain.record.setrecord.persist.SetRecordRepository;
import com.example.fitnessrecord.domain.record.trainingrecord.persist.TrainingRecord;
import com.example.fitnessrecord.domain.record.trainingrecord.persist.TrainingRecordRepository;
import com.example.fitnessrecord.domain.record.volume.persist.VolumeRecord;
import com.example.fitnessrecord.domain.record.volume.persist.VolumeRecordRepository;
import com.example.fitnessrecord.domain.training.common.type.BodyPart;
import com.example.fitnessrecord.global.util.PageConstant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    log.info("[volume recording start] TIME: {}", LocalDateTime.now());

    boolean hasNextPage = true;
    int count = 0;
    while (hasNextPage) {
      Page<TrainingRecord> trainingRecords =
          trainingRecordRepository.findAllByDateBeforeAndVolumeSavedYnIsFalse(
              LocalDate.now(),
              PageRequest.of(0, PageConstant.VOLUME_PROCESS_SIZE));

      count += this.executeSaveVolumes(trainingRecords.getContent());

      hasNextPage = trainingRecords.hasNext();
    }

    log.info("[volume recording end] AMOUNT: {}, TIME: {}", count, LocalDateTime.now());
  }

  private int executeSaveVolumes(List<TrainingRecord> trainingRecords) {
    for (TrainingRecord trainingRecord : trainingRecords) {

      List<SetRecord> setRecords = setRecordRepository.findByTrainingRecord(trainingRecord);

      Map<BodyPart, Double> volumeMap = this.getVolumeMapBySetRecords(setRecords);

      VolumeRecord volumeRecord = this.createVolumeRecord(volumeMap);
      VolumeRecord saved = this.saveVolumeRecord(trainingRecord, volumeRecord);

      log.info("volume record saved: {}", saved);

      trainingRecord.setVolumeSavedYn(true);
      trainingRecordRepository.save(trainingRecord);
    }
    return trainingRecords.size();
  }

  private Map<BodyPart, Double> getVolumeMapBySetRecords(List<SetRecord> setRecords) {
    return setRecords.stream()
        .collect(Collectors.toMap(
            item -> item.getBodyPart(), //key Mapper
            item -> item.getReps() * item.getWeight(), //value Mapper
            (v1, v2) -> v1 + v2) //key가 이미 존재할 때 merge 방법
        );

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
