package com.kkangmj.tripleapp.history.controller;

import com.kkangmj.tripleapp.common.PageResponseDto;
import com.kkangmj.tripleapp.history.service.HistoryService;
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
@RequestMapping("/history")
public class HistoryController {

  private final HistoryService historyService;

  @GetMapping("/history/{userId}/{pageId}")
  public ResponseEntity<PageResponseDto<?>> getUserPointHistory(
      @PathVariable("userId")
          @Pattern(
              regexp =
                  "[a-fA-F\\d]{8}-[a-fA-F\\d]{4}-[a-fA-F\\d]{4}-[a-fA-F\\d]{4}-[a-fA-F\\d]{12}")
          String userId,
      @PathVariable("pageId") @Min(0) int pageId) {

    return ResponseEntity.ok(historyService.getUserPointHistory(UUID.fromString(userId), pageId));
  }

  @GetMapping("/history/all/{pageId}")
  public ResponseEntity<PageResponseDto<?>> getPointHistory(
      @PathVariable("pageId") @Min(1) int pageId) {

    return ResponseEntity.ok(historyService.getPointHistory(pageId));
  }
}
