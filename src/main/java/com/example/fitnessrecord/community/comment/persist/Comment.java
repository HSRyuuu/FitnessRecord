package com.example.fitnessrecord.community.comment.persist;

import com.example.fitnessrecord.community.post.persist.Post;
import com.example.fitnessrecord.domain.user.persist.User;
import java.time.LocalDateTime;
import javax.persistence.Entity;

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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "POST_ID")
  private Post post;

  @OneToOne
  @JoinColumn(name = "WRITER_ID")
  private User writer;

  private String text;

  private LocalDateTime createDateTime;
  private LocalDateTime lastModifiedDateTime;

}
