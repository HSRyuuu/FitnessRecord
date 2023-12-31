package com.example.fitnessrecord.domain.record.volume.service;

import com.example.fitnessrecord.domain.record.setrecord.dto.SetRecordUpdateDto;
import com.example.fitnessrecord.domain.record.setrecord.persist.SetRecord;
import com.example.fitnessrecord.domain.record.trainingrecord.persist.TrainingRecord;
import java.util.List;

public interface VolumeRecordService {

  /**
   * volumeRecord 저장
   */
  int executeVolumeRecordProcess(List<TrainingRecord> trainingRecords);

  /**
   * volumeRecord 수정
   */
  void updateVolumeRecord(TrainingRecord trainingRecord);
}
