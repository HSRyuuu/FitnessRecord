package com.example.fitnessrecord.domain.notification.persist;

import com.example.fitnessrecord.domain.notification.type.EventType;
import com.example.fitnessrecord.domain.user.persist.User;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
public class Notification {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "USER_ID")
  private User user;

  @Enumerated(EnumType.STRING)
  private EventType eventType;

  private String message;

  private LocalDateTime dateTime;

  private boolean checkYn; //알림 확인 여부

}
