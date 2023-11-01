package com.example.fitnessrecord.domain.record.volume.service;

import com.example.fitnessrecord.domain.record.trainingrecord.persist.TrainingRecord;
import java.util.List;

public interface VolumeRecordService {

  int executeVolumeRecordProcess(List<TrainingRecord> trainingRecords);

  boolean updateVolumeRecordByTrainingRecordId(Long trainingRecordId);

}
