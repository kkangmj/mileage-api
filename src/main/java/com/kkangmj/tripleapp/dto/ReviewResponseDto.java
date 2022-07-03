package com.kkangmj.tripleapp.dto;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewResponseDto implements EventResponseDto {
  private UUID userId;

  public static ReviewResponseDto of(UUID userId) {
    return new ReviewResponseDto(userId);
  }
}
