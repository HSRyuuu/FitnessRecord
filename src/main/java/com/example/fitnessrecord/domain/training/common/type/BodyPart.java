package com.example.fitnessrecord.domain.training.common.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.StreamingHttpOutputMessage.Body;

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
  public static BodyPart validation(String input){
    return Stream.of(BodyPart.values())
        .filter(bodyPart -> bodyPart.toString().equals(input.toUpperCase()))
        .findFirst()
        .orElse(null);
  }
}
