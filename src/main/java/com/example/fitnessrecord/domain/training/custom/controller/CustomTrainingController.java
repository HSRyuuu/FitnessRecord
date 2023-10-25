package com.example.fitnessrecord.domain.training.custom.controller;

import com.example.fitnessrecord.domain.training.custom.service.CustomTrainingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CustomTrainingController {

  private final CustomTrainingService customTrainingService;
  
 
}
