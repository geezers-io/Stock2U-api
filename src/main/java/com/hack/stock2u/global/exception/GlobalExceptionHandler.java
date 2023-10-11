package com.hack.stock2u.global.exception;

import com.hack.stock2u.user.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentTypeMismatchException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BasicException.class)
  protected ResponseEntity<BasicErrorResponse> handleBasicException(BasicException ex) {
    BasicErrorResponse res = ex.getErrorResponse();
    return ResponseEntity.status(res.httpStatus()).body(res);
  }

  @ExceptionHandler({MethodArgumentTypeMismatchException.class, IllegalArgumentException.class})
  protected ResponseEntity<BasicErrorResponse> handleIllegalArgsException(Exception ex) {
    String message = ex.getLocalizedMessage();
    String errorMessage = getSimpleMessage(message, IllegalArgumentException.class.getName());
    BasicErrorResponse res =
        new BasicErrorResponse("FAULT_ARG", errorMessage, HttpStatus.BAD_REQUEST);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  public ResponseEntity<BasicErrorResponse> handleUsernameNotFoundException() {
    BasicException ex = UserException.NOT_FOUND_USER.create();
    BasicErrorResponse res = ex.getErrorResponse();
    return ResponseEntity.status(res.httpStatus()).body(res);
  }

  private String getSimpleMessage(String fullText, String exName) {
    int startIdx = fullText.indexOf(exName);
    if (startIdx == -1) {
      return fullText;
    }
    return fullText.substring(startIdx + exName.length() + 2);
  }

}