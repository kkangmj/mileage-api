package com.kkangmj.tripleapp.dto;

import com.kkangmj.tripleapp.domain.UserPoint;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPointResponseDto {
  private UUID id;
  private int contentPoint;
  private int bonusPoint;
  private int totalPoint;

  public static UserPointResponseDto from(UserPoint userPoint) {
    return new UserPointResponseDto(
        userPoint.getUser().getUuid(),
        userPoint.getContentPoint(),
        userPoint.getBonusPoint(),
        userPoint.getContentPoint() + userPoint.getBonusPoint());
  }
}
