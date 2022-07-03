package com.kkangmj.tripleapp.error.exception;

import com.kkangmj.tripleapp.error.ErrorCode;
import org.springframework.http.HttpStatus;

public class InvalidArgumentException extends ApplicationException {

  public InvalidArgumentException(ErrorCode errorCode) {
    super(HttpStatus.BAD_REQUEST, errorCode.getCode(), errorCode.getMessage());
  }
}
