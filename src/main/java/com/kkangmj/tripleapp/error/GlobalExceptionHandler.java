package com.kkangmj.tripleapp.error;

import com.kkangmj.tripleapp.error.exception.ApplicationException;
import javax.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ApplicationException.class)
  protected ResponseEntity<FailureResponseDto> handleApplicationException(ApplicationException e) {
    return ResponseEntity.status(e.getHttpStatus())
        .body(new FailureResponseDto(e.getErrorCode(), e.getMessage()));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  protected ResponseEntity<FailureResponseDto> handleConstraintViolationException(
      ConstraintViolationException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(
            new FailureResponseDto(
                ErrorCode.INVALID_PARAMETER.getCode(),
                ErrorCode.INVALID_PARAMETER.getMessage()
                    + e.getConstraintViolations()
                        .iterator()
                        .next()
                        .getPropertyPath()
                        .toString()
                        .split("\\.")[1]));
  }

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<FailureResponseDto> handleException(Exception e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(
            new FailureResponseDto(
                ErrorCode.INTERNAL_SERVER_ERROR.getCode(),
                ErrorCode.INTERNAL_SERVER_ERROR.getMessage()));
  }
}
