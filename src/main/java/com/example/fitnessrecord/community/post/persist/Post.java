package com.example.fitnessrecord.community.post.persist;

import com.example.fitnessrecord.domain.user.persist.User;
import java.time.LocalDateTime;
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
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "USER_ID")
  private User user;

  private String title;
  private String content;

  private LocalDateTime createDateTime;
  private LocalDateTime lastModifiedDateTime;

  private Integer views;
}
