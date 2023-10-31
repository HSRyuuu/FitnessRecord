package com.example.fitnessrecord.domain.record.setrecord.persist;


import com.example.fitnessrecord.domain.record.trainingrecord.persist.TrainingRecord;
import com.example.fitnessrecord.domain.training.common.type.BodyPart;
import com.example.fitnessrecord.domain.user.persist.User;
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
@Entity
public class SetRecord {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "TRAINING_RECORD_ID")
  private TrainingRecord trainingRecord; //전체 운동

  @ManyToOne
  @JoinColumn(name = "USER_ID")
  private User user;

  private LocalDate date;

  @Enumerated(EnumType.STRING)
  private BodyPart bodyPart; //운동 부위
  private String trainingName; //운동 이름

  private int reps; //반복횟수
  private double weight; //무게

  private String memo; //개인 메모

}
