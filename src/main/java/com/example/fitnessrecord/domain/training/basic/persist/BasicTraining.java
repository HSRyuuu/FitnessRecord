package com.example.fitnessrecord.domain.training.basic.persist;

import com.example.fitnessrecord.domain.training.common.type.BodyPart;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class BasicTraining {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String trainingName;
  private String trainingNameKor;

  @Enumerated(EnumType.STRING)
  private BodyPart bodyPart;

}