package com.example.fitnessrecord.domain.training.common.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BodyPart {
  CHEST("가슴(Chest)"),
  BACK("등(Back)"),
  LEGS("하체(Legs)"),
  SHOULDER("어깨(Shoulder)"),
  BICEPS("이두(Biceps)"),
  TRICEPS("삼두(Triceps)"),
  ABS("복근(Abs)"),
  ETC("기타(Etc)");

  private final String description;

  @JsonCreator
  public static BodyPart of(String input) {
    if (Stream.of(BodyPart.values())
        .anyMatch(bodyPart -> bodyPart.name().equals(input.toUpperCase()))
    ) {
      return BodyPart.valueOf(input.toUpperCase());
    }
    return null;
  }
}
