package com.example.fitnessrecord.domain.statistics.service;

import com.example.fitnessrecord.domain.record.volume.dto.VolumeRecordDto;
import java.time.LocalDate;
import java.util.List;

public interface StatisticsService {

  List<VolumeRecordDto> getVolumeStatistics(LocalDate d1, LocalDate d2, Long userId, Integer page);

}
