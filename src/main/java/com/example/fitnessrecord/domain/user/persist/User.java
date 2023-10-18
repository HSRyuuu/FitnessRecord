package com.example.fitnessrecord.domain.user.persist;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "USER_ID")
  private Long id;

  @Column(unique = true)
  private String email;
  private String password;

  private double height;
  private double weight;

  private double muscleMass; //골격근량
  private double fatMass; //체지방량
  private double fatPercent; //체지방률

  //이메일 인증 관련
  private boolean emailAuthYn;
  private LocalDateTime emailAuthDt;
  private String emailAuthKey;

  @Enumerated(EnumType.STRING)
  private UserType userType;

  private LocalDateTime createdAt;
  private LocalDateTime lastModifiedAt;


}
