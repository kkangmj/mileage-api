package com.kkangmj.tripleapp.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ping")
public class HelperController {

  private final HelperService helperService;

  @GetMapping()
  public ResponseEntity<String> getUserPointHistory() {
    helperService.pong();
    return ResponseEntity.ok("pong");
  }
}
