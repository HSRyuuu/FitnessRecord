package com.example.fitnessrecord.domain.routine.persist;

import com.example.fitnessrecord.domain.user.persist.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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
public class Routine {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ROUTINE_ID")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "USER_ID")
  private User user;

  private String routineName;

  private LocalDate lastModifiedDate;

  @OneToMany(mappedBy = "routine", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<RoutineElement> list = new ArrayList<>();

  @Builder
  private Routine(Long id, User user, String routineName, LocalDate lastModifiedDate) {
    this.id = id;
    this.user = user;
    this.routineName = routineName;
    this.lastModifiedDate = lastModifiedDate;
  }
}
