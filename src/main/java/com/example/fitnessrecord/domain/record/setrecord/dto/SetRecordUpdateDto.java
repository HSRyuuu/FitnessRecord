package com.example.fitnessrecord.domain.record.setrecord.dto;

import com.example.fitnessrecord.domain.training.common.type.BodyPart;
import lombok.Data;

@Data
public class SetRecordUpdateDto {
  private BodyPart bodyPartBefore;
  private double volumeBefore;

  private BodyPart bodyPartAfter;
  private double volumeAfter;

  public SetRecordUpdateDto(BodyPart bodyPartBefore, double volumeBefore) {
    this.bodyPartBefore = bodyPartBefore;
    this.volumeBefore = volumeBefore;
  }
}
