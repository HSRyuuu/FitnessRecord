package com.example.fitnessrecord.domain.user.persist;

import com.example.fitnessrecord.domain.user.type.UserType;
import com.example.fitnessrecord.domain.userbodyinfo.persist.BodyInfo;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "USER_ID")
  private Long id;

  @Column(unique = true)
  private String email;
  private String password;

  private String nickname;

  @OneToOne
  @JoinColumn(name = "BODY_INFO_ID")
  private BodyInfo bodyInfo;

  //이메일 인증 관련
  private boolean emailAuthYn;
  private LocalDateTime emailAuthDateTime;
  private String emailAuthKey;

  @Enumerated(EnumType.STRING)
  private UserType userType;

  private LocalDateTime createdAt;
  private LocalDateTime lastModifiedAt;


}
