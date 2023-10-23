package com.example.fitnessrecord.domain.userbodyinfo.service;


import com.example.fitnessrecord.domain.userbodyinfo.dto.BodyInfoDto;
import com.example.fitnessrecord.domain.userbodyinfo.dto.BodyInfoInput;
import java.time.LocalDate;
import java.util.List;

public interface BodyInfoService {

  /**
   * BodyInfo 추가하기 - 하루에 한번만 추가 가능 - 하루에 여러번 추가 시에는 해당 일자의 기존 데이터를 수정
   */
  BodyInfoDto addBodyInfo(Long userId, BodyInfoInput input);

  /**
   * 특정 날짜의 BodyInfo 데이터 삭제
   */
  BodyInfoDto deleteByDate(Long userId, LocalDate date);

  /**
   * start 부터 end 까지의 Body Info 기록
   */
  List<BodyInfoDto> bodyInfoListByPeriod(Long userId, LocalDate start, LocalDate end);


}
