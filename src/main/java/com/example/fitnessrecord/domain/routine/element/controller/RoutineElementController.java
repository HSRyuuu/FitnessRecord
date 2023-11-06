package com.example.fitnessrecord.domain.routine.element.controller;

import com.example.fitnessrecord.domain.routine.element.dto.AddRoutineElementInput;
import com.example.fitnessrecord.domain.routine.element.dto.RoutineElementDto;
import com.example.fitnessrecord.domain.routine.element.service.RoutineElementService;
import com.example.fitnessrecord.global.auth.sercurity.principal.PrincipalDetails;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
public class RoutineElementController {

  private final RoutineElementService routineElementService;

  @ApiOperation("루틴 요소 1개 추가")
  @PostMapping("/routine/element")
  public ResponseEntity<?> addRoutineElements(@Valid @RequestBody AddRoutineElementInput input,
      @AuthenticationPrincipal PrincipalDetails principalDetails){
    List<RoutineElementDto> result =
        routineElementService.addRoutineElement(input, principalDetails.getUserId());

    return ResponseEntity.ok(result);
  }

}