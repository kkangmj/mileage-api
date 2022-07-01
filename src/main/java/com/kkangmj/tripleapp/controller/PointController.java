package com.kkangmj.tripleapp.controller;

import com.kkangmj.tripleapp.dto.UserPointResponseDto;
import com.kkangmj.tripleapp.service.PointService;
import java.util.UUID;
import javax.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/point")
public class PointController {

  private final PointService userPointService;

  @GetMapping("/{userId}")
  public ResponseEntity<UserPointResponseDto> getUserPoint(
      @PathVariable("userId")
          @Pattern(
              regexp =
                  "[a-fA-F\\d]{8}-[a-fA-F\\d]{4}-[a-fA-F\\d]{4}-[a-fA-F\\d]{4}-[a-fA-F\\d]{12}")
          String userId) {
    return ResponseEntity.ok(userPointService.getUserPoint(UUID.fromString(userId)));
  }
}
