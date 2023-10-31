package com.example.fitnessrecord.domain.record.volume.persist;

import com.example.fitnessrecord.domain.record.trainingrecord.persist.TrainingRecord;
import com.example.fitnessrecord.domain.user.persist.User;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Entity
public class VolumeRecord {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "TRAINING_RECORD_ID")
  private TrainingRecord trainingRecord;

  @ManyToOne
  @JoinColumn(name = "USER_ID")
  private User user;

  private LocalDate date;

  private boolean weeklyRecordedYn;

  private double chest;
  private double back;
  private double legs;
  private double shoulder;
  private double biceps;
  private double triceps;
  private double etc;

  @Builder
  public VolumeRecord(double chest, double back, double legs,
      double shoulder, double biceps, double triceps, double etc) {
    this.chest = chest;
    this.back = back;
    this.legs = legs;
    this.shoulder = shoulder;
    this.biceps = biceps;
    this.triceps = triceps;
    this.etc = etc;
  }

  @Override
  public String toString() {
    return "VolumeRecord{" +
        "id=" + id +
        "date=" + date +
        ", chest=" + chest +
        ", back=" + back +
        ", legs=" + legs +
        ", shoulder=" + shoulder +
        ", biceps=" + biceps +
        ", triceps=" + triceps +
        ", etc=" + etc +
        '}';
  }
}
