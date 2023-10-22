package com.example.fitnessrecord.domain.userbodyinfo.service;


import com.example.fitnessrecord.domain.userbodyinfo.dto.BodyInfoDto;
import com.example.fitnessrecord.domain.userbodyinfo.dto.BodyInfoInput;

public interface BodyInfoService {

  /**
   * BodyInfo 추가하기
   * - 하루에 한번만 추가 가능
   * - 하루에 여러번 추가 시에는 해당 일자의 기존 데이터를 수정
   */
  BodyInfoDto addBodyInfo(Long userId, BodyInfoInput input);

}
