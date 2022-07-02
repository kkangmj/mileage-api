package com.kkangmj.tripleapp.dto;

import java.util.Date;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPointHistoryDto {
  private UUID userId;
  private int contentPoint;
  private int bonusPoint;
  private Date timestamp;

  public static UserPointHistoryDto of(
      UUID userId, int contentPoint, int bonusPoint, Date timestamp) {
    return new UserPointHistoryDto(userId, contentPoint, bonusPoint, timestamp);
  }
}
