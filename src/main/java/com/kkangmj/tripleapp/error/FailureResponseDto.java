package com.kkangmj.tripleapp.error;

import lombok.Getter;

@Getter
public class FailureResponseDto {

  private final String code;
  private final String message;

  public FailureResponseDto(String code, String message) {
    this.code = code;
    this.message = message;
  }
}
