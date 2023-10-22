package com.example.fitnessrecord.domain.userbodyinfo.dto;

import com.example.fitnessrecord.domain.userbodyinfo.persist.BodyInfo;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BodyInfoInput {

  private double height;
  private double weight;

  private double muscleMass; //골격근량
  private double fatMass; //체지방량

  public static BodyInfo toEntity(BodyInfoInput input) {
    return BodyInfo.builder()
        .height(input.getHeight())
        .weight(input.getWeight())
        .muscleMass(input.getMuscleMass())
        .fatMass(input.getFatMass())
        .createDate(LocalDate.now())
        .build();
  }

  public static void updateBodyInfo(BodyInfo bodyInfo, BodyInfoInput input) {
    bodyInfo.setHeight(input.getHeight());
    bodyInfo.setWeight(input.getWeight());
    bodyInfo.setMuscleMass(input.getMuscleMass());
    bodyInfo.setFatMass(input.getFatMass());
  }
}
