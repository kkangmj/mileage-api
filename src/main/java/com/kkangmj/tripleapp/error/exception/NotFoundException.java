package com.kkangmj.tripleapp.error.exception;

import com.kkangmj.tripleapp.error.ErrorCode;
import com.kkangmj.tripleapp.error.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class NotFoundException extends ApplicationException {

  public NotFoundException(ErrorCode errorCode) {
    super(HttpStatus.NOT_FOUND, errorCode.getCode(), errorCode.getMessage());
  }
}
