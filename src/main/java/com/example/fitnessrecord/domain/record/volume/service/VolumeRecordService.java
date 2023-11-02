package com.example.fitnessrecord.domain.record.volume.service;

import com.example.fitnessrecord.domain.record.setrecord.dto.SetRecordUpdateRequest;
import com.example.fitnessrecord.domain.record.setrecord.persist.SetRecord;
import com.example.fitnessrecord.domain.record.trainingrecord.persist.TrainingRecord;
import java.util.List;

public interface VolumeRecordService {

  int executeVolumeRecordProcess(List<TrainingRecord> trainingRecords);
  void updateVolumeRecordForAdd(TrainingRecord trainingRecord, SetRecord setRecord);
  void updateVolumeRecordForDelete(TrainingRecord trainingRecord, SetRecord setRecord);
  void updateVolumeRecordForUpdate(TrainingRecord trainingRecord, SetRecordUpdateRequest request);
}
