package com.example.fitnessrecord.domain.record.trainingrecord.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 특정 기간의 운동 기록을 반환할 때 사용한다.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingRecordListResponse {

  private LocalDate start;
  private LocalDate end;

  private int currentPage;
  private int totalPages;
  private int totalElements;

  private List<TrainingRecordResponse> list;

}
