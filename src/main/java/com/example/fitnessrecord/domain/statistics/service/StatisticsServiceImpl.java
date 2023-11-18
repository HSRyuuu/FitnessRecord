package com.example.fitnessrecord.domain.statistics.service;

import com.example.fitnessrecord.domain.record.volume.dto.VolumeRecordDto;
import com.example.fitnessrecord.domain.record.volume.persist.VolumeRecord;
import com.example.fitnessrecord.domain.record.volume.persist.VolumeRecordRepository;
import com.example.fitnessrecord.global.exception.ErrorCode;
import com.example.fitnessrecord.global.exception.MyException;
import com.example.fitnessrecord.global.util.PageConstant;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class StatisticsServiceImpl implements StatisticsService {

  private final VolumeRecordRepository volumeRecordRepository;

  @Override
  public List<VolumeRecordDto> getVolumeStatistics(LocalDate d1, LocalDate d2, Long userId,
      Integer page) {

    Page<VolumeRecord> volumeRecords = volumeRecordRepository.findAllByUserIdAndDateBetween(
        userId, d1, d2, PageRequest.of(page - 1, PageConstant.DEFAULT_PAGE_SIZE));

    return volumeRecords.stream()
        .map(v -> VolumeRecordDto.fromEntity(v, userId))
        .collect(Collectors.toList());

  }
}
