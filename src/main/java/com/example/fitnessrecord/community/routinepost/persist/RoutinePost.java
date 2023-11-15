package com.example.fitnessrecord.community.routinepost.persist;

import com.example.fitnessrecord.domain.routine.routine.persist.Routine;
import com.example.fitnessrecord.domain.user.persist.User;
import java.time.LocalDateTime;
import javax.persistence.Entity;

import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class RoutinePost {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "USER_ID")
  private User user;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ROUTINE_ID")
  private Routine routine;

  private String title;
  private String content;

  private LocalDateTime createDateTime;
  private LocalDateTime lastModifiedDateTime;

  private int views;
  private int likes;

  public void addViews(){
    this.views += 1;
  }

}
