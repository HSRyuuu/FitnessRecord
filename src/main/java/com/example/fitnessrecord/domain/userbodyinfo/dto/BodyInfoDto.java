package com.example.fitnessrecord.domain.userbodyinfo.dto;

import com.example.fitnessrecord.domain.userbodyinfo.persist.BodyInfo;
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
public class BodyInfoDto {

  private Long id;

  private Long userId;

  private double height;
  private double weight;

  private double muscleMass; //골격근량
  private double fatMass; //체지방량

  public static BodyInfoDto fromEntity(BodyInfo bodyInfo) {
    return BodyInfoDto.builder()
        .id(bodyInfo.getId())
        .userId(bodyInfo.getUser().getId())
        .height(bodyInfo.getHeight())
        .weight(bodyInfo.getWeight())
        .muscleMass(bodyInfo.getMuscleMass())
        .fatMass(bodyInfo.getFatMass())
        .build();
  }
}
