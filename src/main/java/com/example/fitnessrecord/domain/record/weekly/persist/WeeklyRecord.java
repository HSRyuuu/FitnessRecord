package com.example.fitnessrecord.domain.record.weekly.persist;

import com.example.fitnessrecord.domain.user.persist.User;
import java.time.LocalDate;
import javax.persistence.Entity;
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
public class WeeklyRecord {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "USER_ID")
  private User user;

  private int trainingPerWeek;

  private LocalDate startDate;
  private LocalDate endDate;

  //주간 volume 기록
  private double chest;
  private double back;
  private double arms;
  private double legs;
  private double shoulder;
  private double biceps;
  private double triceps;
  private double etc;

}
