package com.kkangmj.tripleapp.dto;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewResponseDto implements EventResponseDto {
  private UUID userId;
  private UUID reviewId;

  public static ReviewResponseDto of(UUID userId, UUID reviewId) {
    return new ReviewResponseDto(userId, reviewId);
  }
}
