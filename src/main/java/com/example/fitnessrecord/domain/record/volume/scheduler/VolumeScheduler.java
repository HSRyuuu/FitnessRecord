package com.example.fitnessrecord.domain.record.volume.scheduler;

import com.example.fitnessrecord.domain.record.trainingrecord.persist.TrainingRecord;
import com.example.fitnessrecord.domain.record.trainingrecord.persist.TrainingRecordRepository;
import com.example.fitnessrecord.domain.record.volume.service.VolumeRecordService;
import com.example.fitnessrecord.global.util.PageConstant;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
  private final VolumeRecordService volumeRecordService;

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

      count += volumeRecordService.executeSaveVolumes(trainingRecords.getContent());

      hasNextPage = trainingRecords.hasNext();
    }

    log.info("[volume recording end] AMOUNT: {}, TIME: {}", count, LocalDateTime.now());
  }

}
