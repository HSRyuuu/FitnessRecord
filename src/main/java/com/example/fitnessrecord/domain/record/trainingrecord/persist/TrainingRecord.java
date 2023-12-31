package com.example.fitnessrecord.domain.record.trainingrecord.persist;


import com.example.fitnessrecord.domain.record.setrecord.persist.SetRecord;
import com.example.fitnessrecord.domain.user.persist.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
public class TrainingRecord {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "USER_ID")
  private User user;

  private LocalDate date; //운동 날짜

  private LocalDate lastModifiedDate;

  @OneToMany(mappedBy = "trainingRecord", cascade = CascadeType.REMOVE)
  private List<SetRecord> setRecords = new ArrayList<>();

  @Builder
  public TrainingRecord(Long id, User user, LocalDate date, LocalDate lastModifiedDate) {
    this.id = id;
    this.user = user;
    this.date = date;
    this.lastModifiedDate = lastModifiedDate;
  }
}
