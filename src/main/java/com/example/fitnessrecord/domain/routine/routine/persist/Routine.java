package com.example.fitnessrecord.domain.routine.routine.persist;

import com.example.fitnessrecord.domain.routine.element.persist.RoutineElement;
import com.example.fitnessrecord.domain.user.persist.User;
import java.time.LocalDateTime;
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

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "USER_ID")
  private User user;

  private String routineName;

  private LocalDateTime lastModifiedDateTime;

  private String description;

  @OneToMany(mappedBy = "routine", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<RoutineElement> list = new ArrayList<>();

  @Builder
  private Routine(User user, String routineName, LocalDateTime lastModifiedDateTime, String description) {
    this.user = user;
    this.routineName = routineName;
    this.lastModifiedDateTime = lastModifiedDateTime;
    this.description = description;
  }
}
