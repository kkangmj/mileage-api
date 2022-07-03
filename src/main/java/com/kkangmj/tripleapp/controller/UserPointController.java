package com.kkangmj.tripleapp.controller;

import com.kkangmj.tripleapp.dto.PageResponseDto;
import com.kkangmj.tripleapp.dto.UserPointResponseDto;
import com.kkangmj.tripleapp.service.UserPointService;
import java.util.UUID;
import javax.validation.constraints.Min;
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
public class UserPointController {

  private final UserPointService userPointService;

  @GetMapping("/{userId}")
  public ResponseEntity<UserPointResponseDto> getUserPoint(
      @PathVariable("userId")
          @Pattern(
              regexp =
                  "[a-fA-F\\d]{8}-[a-fA-F\\d]{4}-[a-fA-F\\d]{4}-[a-fA-F\\d]{4}-[a-fA-F\\d]{12}")
          String userId) {
    return ResponseEntity.ok(userPointService.getUserPoint(UUID.fromString(userId)));
  }

  @GetMapping("/history/{userId}/{pageId}")
  public ResponseEntity<PageResponseDto<?>> getUserPointHistory(
      @PathVariable("userId")
          @Pattern(
              regexp =
                  "[a-fA-F\\d]{8}-[a-fA-F\\d]{4}-[a-fA-F\\d]{4}-[a-fA-F\\d]{4}-[a-fA-F\\d]{12}")
          String userId,
      @PathVariable("pageId") @Min(0) int pageId) {

    return ResponseEntity.ok(userPointService.getUserPointHistory(UUID.fromString(userId), pageId));
  }

  @GetMapping("/history/all/{pageId}")
  public ResponseEntity<PageResponseDto<?>> getAllPointHistory(
      @PathVariable("pageId") @Min(1) int pageId) {

    return ResponseEntity.ok(userPointService.getAllPointHistory(pageId));
  }
}
