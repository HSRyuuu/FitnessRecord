package com.example.fitnessrecord.domain.record.weekly.scheduler;

import com.example.fitnessrecord.domain.record.volume.persist.VolumeRecord;
import com.example.fitnessrecord.domain.record.volume.persist.VolumeRecordRepository;
import com.example.fitnessrecord.domain.record.weekly.persist.WeeklyRecord;
import com.example.fitnessrecord.domain.record.weekly.persist.WeeklyRecordRepository;
import com.example.fitnessrecord.domain.user.persist.User;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class WeeklyRecordScheduler {

  private final VolumeRecordRepository volumeRecordRepository;
  private final WeeklyRecordRepository weeklyRecordRepository;

  /**
   * 지지난주 월요일 ~ 일요일의 데이터를 저장
   */
  @Scheduled(cron = "0 0 4 * * 2") //매주 화요일 오전 4시에 실행
  public void saveWeeklyRecords(){
    LocalDate today = LocalDate.now();
    LocalDate lastSunday = today.minusDays(today.getDayOfWeek().getValue()).minusDays(7);
    LocalDate lastMonday = lastSunday.minusDays(6);
    log.info("[recoding WeeklyRecord start] date: {} ~ {}, now: {}", lastMonday, lastSunday, LocalDateTime.now());

    //이부분을 while(true)로 하고 find.isEmpty()일 때 break 되도록 해도 될까요?
    //뭔가 while(true)를 쓰기 꺼려지는 느낌이 듭니다..
    //아니면 다른 방법이 있을까요?
    //그리고 이 로직을 수행할때는
    while(true){
      Optional<VolumeRecord> find =
          volumeRecordRepository
              .findFirstByDateBetweenAndWeeklyRecordedYnIsFalse(lastMonday, lastSunday);

      if(find.isEmpty()){
        break;
      }
      User user = find.get().getUser();

      log.info("find user: {}", user.getEmail());

      List<VolumeRecord> volumeRecords = volumeRecordRepository.findAllByUserAndDateBetween(
          user, lastMonday, lastSunday);

      WeeklyRecord weeklyRecord = this.createAndSetWeeklyRecordValues(volumeRecords);
      weeklyRecord.setUser(user);
      weeklyRecord.setStartDate(lastMonday);
      weeklyRecord.setEndDate(lastSunday);

      weeklyRecordRepository.save(weeklyRecord);
      log.info("user: {} record complete", user.getEmail());
    }
    log.info("[recoding WeeklyRecord finished] time: {}", LocalDateTime.now());


  }
  private WeeklyRecord createAndSetWeeklyRecordValues(List<VolumeRecord> list){
    WeeklyRecord weeklyRecord = new WeeklyRecord();
    weeklyRecord.setTrainingPerWeek(list.size());

    list.stream().forEach(volumeRecord -> {
      weeklyRecord.addVolumesValues(volumeRecord);
      volumeRecord.setWeeklyRecordedYn(true);
    });

    volumeRecordRepository.saveAll(list);

    return weeklyRecord;
  }
}
