package com.example.fitnessrecord.domain.record.volume.service;

import com.example.fitnessrecord.domain.record.setrecord.dto.SetRecordUpdateDto;
import com.example.fitnessrecord.domain.record.setrecord.persist.SetRecord;
import com.example.fitnessrecord.domain.record.trainingrecord.persist.TrainingRecord;
import java.util.List;

public interface VolumeRecordService {

  int executeVolumeRecordProcess(List<TrainingRecord> trainingRecords);
  void updateVolumeRecord(TrainingRecord trainingRecord);
}
