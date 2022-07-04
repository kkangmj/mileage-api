package com.kkangmj.tripleapp.event.controller;

import com.kkangmj.tripleapp.error.ErrorCode;
import com.kkangmj.tripleapp.error.exception.InvalidArgumentException;
import com.kkangmj.tripleapp.event.dto.EventRequestDto;
import com.kkangmj.tripleapp.event.dto.EventResponseDto;
import com.kkangmj.tripleapp.event.service.EventService;
import com.kkangmj.tripleapp.review.service.ReviewService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

  private final ReviewService reviewService;

  @PostMapping
  public ResponseEntity<? extends EventResponseDto> handleEvent(
      @RequestBody @Valid EventRequestDto eventRequestDto) {

    return ResponseEntity.ok(
        getEventService(eventRequestDto.getEventType()).handleEvent(eventRequestDto));
  }

  private EventService getEventService(EventType eventType) {
    switch (eventType) {
      case REVIEW:
        return reviewService;
      default:
        throw new InvalidArgumentException(ErrorCode.INVALID_PARAMETER);
    }
  }
}
