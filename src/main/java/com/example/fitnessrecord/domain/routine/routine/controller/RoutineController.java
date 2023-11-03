package com.example.fitnessrecord.domain.routine.routine.controller;

import com.example.fitnessrecord.domain.routine.routine.dto.AddRoutineResult;
import com.example.fitnessrecord.domain.routine.routine.service.RoutineService;
import com.example.fitnessrecord.global.auth.sercurity.principal.PrincipalDetails;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RoutineController {

  private final RoutineService routineService;

  @ApiOperation("Routine을 하나 추가한다.")
  @PostMapping("/routine")
  public ResponseEntity<?> addRoutine(@RequestBody String name,
      @AuthenticationPrincipal PrincipalDetails principalDetails) {

    AddRoutineResult result =
        routineService.addRoutine(principalDetails.getUserId(), name);

    return ResponseEntity.ok(result);
  }


}
