package com.example.fitnessrecord.domain.record.setrecord.dto;

import com.example.fitnessrecord.domain.record.setrecord.persist.SetRecord;
import com.example.fitnessrecord.domain.record.trainingrecord.persist.TrainingRecord;
import com.example.fitnessrecord.domain.training.common.type.BodyPart;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SetRecordInput {

  @NotNull(message = "유효하지 않은 운동 부위(BodyPart)가 입력되었습니다.")
  private BodyPart bodyPart;

  @NotNull(message = "운동 명(trainingName)은 필수 값입니다.")
  private String trainingName;

  @NotNull(message = "reps를 입력해주세요.")
  @Range(min = 1L, max = 9999L, message = "reps는 1~9999의 숫자만 입력 가능합니다.")
  private int reps;

  @NotNull(message = "무게를 입력해주세요.")
  @Range(min = 0L, max = 9999L, message = "무게는 0~9999의 숫자만 입력 가능합니다.")
  private double weight;

  private String memo;

  public static SetRecord toEntity(SetRecordInput input, TrainingRecord trainingRecord){
    return SetRecord.builder()
        .trainingRecord(trainingRecord)
        .user(trainingRecord.getUser())
        .date(trainingRecord.getDate())
        .bodyPart(input.getBodyPart())
        .trainingName(input.getTrainingName())
        .reps(input.getReps())
        .weight(input.getWeight())
        .build();
  }

}
