package com.example.fitnessrecord.domain.userbodyinfo.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class BodyInfoListResult {

  private Long userId;
  private String userEmail;
  private LocalDate start;
  private LocalDate end;

  private List<BodyInfoDto> list;

}
