package com.example.fitnessrecord.domain.record.trainingrecord.service;

import com.example.fitnessrecord.domain.record.setrecord.dto.SetRecordDto;
import com.example.fitnessrecord.domain.record.trainingrecord.dto.TrainingRecordDto;
import com.example.fitnessrecord.domain.record.trainingrecord.dto.TrainingRecordListResponse;
import com.example.fitnessrecord.domain.record.trainingrecord.dto.TrainingRecordResponse;
import java.time.LocalDate;
import java.util.List;

public interface TrainingRecordService {

  /**
   * Training Record 추가
   */
  TrainingRecordDto addTrainingRecord(Long userId, LocalDate date);

  /**
   * Training Record 정보 ( Training Record + Set Records )
   */
  TrainingRecordResponse getTrainingRecordInfo(Long trainingRecordId, String username);

  /**
   * 특정 기간 사이의 TrainingRecord 정보
   */
  TrainingRecordListResponse getTrainingRecordList(Long userId, int page, LocalDate start, LocalDate end);

  /**
   * VolumeRecord 업데이트
   */
  boolean updateVolumeRecord(Long userId, Long trainingRecordId);


}
