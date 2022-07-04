package com.kkangmj.tripleapp.event.service;

import com.kkangmj.tripleapp.event.dto.EventRequestDto;
import com.kkangmj.tripleapp.event.dto.EventResponseDto;

public interface EventService {
  EventResponseDto handleEvent(EventRequestDto eventRequestDto);
}
