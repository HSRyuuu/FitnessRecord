package com.example.fitnessrecord.domain.userbodyinfo.service;


import com.example.fitnessrecord.domain.userbodyinfo.dto.BodyInfoDto;
import com.example.fitnessrecord.domain.userbodyinfo.dto.BodyInfoInput;

public interface BodyInfoService {
  BodyInfoDto addBodyInfo(Long userId, BodyInfoInput input);
}
