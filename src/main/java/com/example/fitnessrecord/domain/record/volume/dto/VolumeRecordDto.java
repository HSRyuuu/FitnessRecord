package com.example.fitnessrecord.domain.record.volume.dto;

import com.example.fitnessrecord.domain.record.volume.persist.VolumeRecord;
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
public class VolumeRecordDto {
  private Long id;
  private Long trainingRecordId;
  private Long userId;
  private LocalDate date;

  private double chest;
  private double back;
  private double legs;
  private double shoulder;
  private double biceps;
  private double triceps;
  private double abs;
  private double etc;

  public static VolumeRecordDto fromEntity(VolumeRecord volumeRecord, Long userId){
    return VolumeRecordDto.builder()
        .id(volumeRecord.getId())
        .userId(userId)
        .date(volumeRecord.getDate())
        .chest(volumeRecord.getChest())
        .back(volumeRecord.getBack())
        .legs(volumeRecord.getLegs())
        .shoulder(volumeRecord.getShoulder())
        .biceps(volumeRecord.getBiceps())
        .triceps(volumeRecord.getTriceps())
        .abs(volumeRecord.getAbs())
        .etc(volumeRecord.getEtc())
        .build();

  }
}
