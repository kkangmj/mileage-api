package com.kkangmj.tripleapp.service;

import com.kkangmj.tripleapp.dto.EventRequestDto;
import com.kkangmj.tripleapp.dto.EventResponseDto;

public interface EventService {
  EventResponseDto handleEvent(EventRequestDto eventRequestDto);
}
