package com.example.fitnessrecord.domain.record.setrecord.dto;


import com.example.fitnessrecord.domain.record.setrecord.persist.SetRecord;
import com.example.fitnessrecord.domain.record.trainingrecord.persist.TrainingRecord;
import com.example.fitnessrecord.domain.training.common.type.BodyPart;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
public class SetRecordDto {

  private Long id;

  private Long trainingRecordId; //전체 운동
  private Long userId;

  private LocalDate date;

  private BodyPart bodyPart; //운동 부위

  private String trainingName; //운동 이름
  private int reps; //반복횟수
  private double weight; //무게

  private String memo; //개인 메모

  public static SetRecordDto fromEntity(SetRecord setRecord){
    return SetRecordDto.builder()
        .id(setRecord.getId())
        .trainingRecordId(setRecord.getTrainingRecord().getId())
        .userId(setRecord.getUser().getId())
        .date(setRecord.getDate())
        .bodyPart(setRecord.getBodyPart())
        .trainingName(setRecord.getTrainingName())
        .reps(setRecord.getReps())
        .weight(setRecord.getWeight())
        .memo(setRecord.getMemo())
        .build();
  }
}
