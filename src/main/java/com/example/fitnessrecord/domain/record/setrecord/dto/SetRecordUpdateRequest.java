package com.example.fitnessrecord.domain.record.setrecord.dto;

import com.example.fitnessrecord.domain.training.common.type.BodyPart;
import lombok.Data;

@Data
public class SetRecordUpdateRequest {
  private BodyPart bodyPartBefore;
  private double volumeBefore;

  private BodyPart bodyPartAfter;
  private double volumeAfter;

  public SetRecordUpdateRequest(BodyPart bodyPartBefore, double volumeBefore) {
    this.bodyPartBefore = bodyPartBefore;
    this.volumeBefore = volumeBefore;
  }
}
