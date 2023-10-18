package com.example.fitnessrecord.domain.training.basic.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BodyPart {
  CHEST("가슴(Chest)"),
  BACK("등(Back)"),
  BICEPS("이두(Biceps)"),
  TRICEPS("삼두(Triceps)"),
  SHOULDER("어깨(Shoulder)"),
  ARM("팔(Arms)"),
  ABS("복근(Abs)"),
  LOWER_BODY("하체(Lower body)"),
  QUADS("대퇴 사두(Quads)"),
  HAMSTRINGS("햄스트링(Hamstrings)"),
  CALVES("종아리(Calves)"),
  ETC("기타(Etc)");

  private final String description;
}
