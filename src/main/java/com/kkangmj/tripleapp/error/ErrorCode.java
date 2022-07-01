package com.kkangmj.tripleapp.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  // BAD_REQUST
  INVALID_PARAMETER("40001", "유효하지 않은 요청값입니다: "),

  // NOT_FOUND
  USER_NOT_FOUND("40401", "존재하지 않는 사용자입니다."),

  // INTERNAL_SERVER_ERROR
  INTERNAL_SERVER_ERROR("50000", "오류가 발생했습니다.");

  private final String code;
  private final String message;
}
