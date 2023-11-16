package com.example.fitnessrecord.community.routinecomment.persist;

import com.example.fitnessrecord.community.routinepost.persist.RoutinePost;
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

@Getter
@Setter
@NoArgsConstructor
@Entity
public class RoutineComment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ROUTINE_POST_ID")
  private RoutinePost routinePost;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "WRITER_ID")
  private User writer;

  private String text;

  private LocalDateTime createDateTime;
  private LocalDateTime lastModifiedDateTime;

  public RoutineComment(RoutinePost routinePost, User user, String text){
    this.routinePost = routinePost;
    this.writer = user;
    this.text = text;
    this.createDateTime = LocalDateTime.now();
    this.lastModifiedDateTime = LocalDateTime.now();
  }

}
