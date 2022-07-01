package com.kkangmj.tripleapp.error.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class ApplicationException extends RuntimeException {
  private final HttpStatus httpStatus;
  private final String errorCode;

  public ApplicationException(HttpStatus httpStatus, String errorCode, String message) {
    super(message);
    this.httpStatus = httpStatus;
    this.errorCode = errorCode;
  }
}
