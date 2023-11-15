package com.example.fitnessrecord.community.routinepost.controller;

import com.example.fitnessrecord.community.routinepost.dto.AddRoutinePostInput;
import com.example.fitnessrecord.community.routinepost.dto.DeleteRoutinePostResult;
import com.example.fitnessrecord.community.routinepost.dto.RoutinePostResult;
import com.example.fitnessrecord.community.routinepost.dto.UpdateRoutinePostInput;
import com.example.fitnessrecord.community.routinepost.service.RoutinePostService;
import com.example.fitnessrecord.global.auth.sercurity.principal.PrincipalDetails;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/board")
@RestController
public class RoutinePostController {

  private final RoutinePostService routinePostService;

  @ApiOperation("루틴 공유 글을 작성한다.")
  @PostMapping("/post")
  public ResponseEntity<?> addRoutinePost(@RequestBody AddRoutinePostInput input,
      @AuthenticationPrincipal PrincipalDetails principalDetails) {
    RoutinePostResult result =
        routinePostService.addRoutinePost(input, principalDetails.getUserId());
    return ResponseEntity.ok(result);
  }

  @ApiOperation(value = "루틴 공유 글을 수정한다.", notes = "Routine은 수정할 수 없다.")
  @PutMapping("/post/{id}")
  public ResponseEntity<?> updateRoutinePost(@PathVariable Long id,
      @RequestBody UpdateRoutinePostInput input,
      @AuthenticationPrincipal PrincipalDetails principalDetails) {

    RoutinePostResult result =
        routinePostService.updateRoutinePost(id, input, principalDetails.getUserId());

    return ResponseEntity.ok(result);
  }

  @ApiOperation("루틴 공유 글을 삭제한다.")
  @DeleteMapping("/post/{id}")
  public ResponseEntity<?> deleteRoutinePost(@PathVariable Long id,
      @AuthenticationPrincipal PrincipalDetails principalDetails){
    DeleteRoutinePostResult result = routinePostService.deleteRoutinePost(id,
        principalDetails.getUserId());
    return ResponseEntity.ok(result);
  }


}
