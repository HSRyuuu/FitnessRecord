package com.example.fitnessrecord.domain.record.volume.service;

import com.example.fitnessrecord.domain.record.setrecord.persist.SetRecord;
import com.example.fitnessrecord.domain.record.setrecord.persist.SetRecordRepository;
import com.example.fitnessrecord.domain.record.trainingrecord.persist.TrainingRecord;
import com.example.fitnessrecord.domain.record.trainingrecord.persist.TrainingRecordRepository;
import com.example.fitnessrecord.domain.record.volume.persist.VolumeRecord;
import com.example.fitnessrecord.domain.record.volume.persist.VolumeRecordRepository;
import com.example.fitnessrecord.domain.training.common.type.BodyPart;
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

  public int executeSaveVolumes(List<TrainingRecord> trainingRecords) {
    for (TrainingRecord trainingRecord : trainingRecords) {
      //이미 volumeRecord가 존재할 때 (이전에 저장 후 수정되었을 때)
      if (volumeRecordRepository.existsByTrainingRecord(trainingRecord)) {
        this.updateVolumeRecord(trainingRecord);
      }//처음 저장할 때
      else{
        this.saveVolumeRecord(trainingRecord);
      }
    }
    return trainingRecords.size();
  }

  public void updateVolumeRecord(TrainingRecord trainingRecord) {
    Optional<VolumeRecord> optionalVolumeRecord =
        volumeRecordRepository.findByTrainingRecord(trainingRecord);
    if (optionalVolumeRecord.isEmpty()) {
      return;
    }
    VolumeRecord volumeRecord = optionalVolumeRecord.get();

    List<SetRecord> setRecords = setRecordRepository.findByTrainingRecord(trainingRecord);

    Map<BodyPart, Double> volumeMap = this.getVolumeMapBySetRecords(setRecords);

    this.setVolumeRecord(volumeRecord, volumeMap);

    VolumeRecord saved = volumeRecordRepository.save(volumeRecord);

    log.info("volume record saved: {}", saved);

    trainingRecord.setVolumeSavedYn(true);
    trainingRecordRepository.save(trainingRecord);
  }

  private void setVolumeRecord(VolumeRecord volumeRecord, Map<BodyPart, Double> volumeMap) {
    volumeRecord.setChest(volumeMap.getOrDefault(BodyPart.CHEST, 0.0));
    volumeRecord.setBack(volumeMap.getOrDefault(BodyPart.BACK, 0.0));
    volumeRecord.setLegs(volumeMap.getOrDefault(BodyPart.LEGS, 0.0));
    volumeRecord.setShoulder(volumeMap.getOrDefault(BodyPart.SHOULDER, 0.0));
    volumeRecord.setBiceps(volumeMap.getOrDefault(BodyPart.BICEPS, 0.0));
    volumeRecord.setTriceps(volumeMap.getOrDefault(BodyPart.TRICEPS, 0.0));
    volumeRecord.setEtc(volumeMap.getOrDefault(BodyPart.ETC, 0.0));
  }

  private void saveVolumeRecord(TrainingRecord trainingRecord){
    List<SetRecord> setRecords = setRecordRepository.findByTrainingRecord(trainingRecord);

    Map<BodyPart, Double> volumeMap = this.getVolumeMapBySetRecords(setRecords);

    VolumeRecord volumeRecord = this.createVolumeRecord(volumeMap);

    VolumeRecord saved = this.save(trainingRecord, volumeRecord);

    log.info("volume record saved: {}", saved);

    trainingRecord.setVolumeSavedYn(true);
    trainingRecordRepository.save(trainingRecord);
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

  private VolumeRecord save(
      TrainingRecord trainingRecord, VolumeRecord volumeRecord) {

    volumeRecord.setTrainingRecord(trainingRecord);
    volumeRecord.setUser(trainingRecord.getUser());
    volumeRecord.setDate(trainingRecord.getDate());

    return volumeRecordRepository.save(volumeRecord);
  }


}
