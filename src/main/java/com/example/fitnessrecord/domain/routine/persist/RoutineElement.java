package com.example.fitnessrecord.domain.routine.persist;


import com.example.fitnessrecord.domain.training.basic.type.BodyPart;
import javax.persistence.Column;
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
@Entity(name = "ROUTINE_ELEMENT")
public class RoutineElement {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ELEMENT_ID")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "ROUTINE_ID")
  private Routine routine;

  private String trainingName; //운동 명

  @Enumerated(EnumType.STRING)
  private BodyPart bodyPart; //운동 부위

  private double weight; //무게
  private Integer reps; //횟수

}
