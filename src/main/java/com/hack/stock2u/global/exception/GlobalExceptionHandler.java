package com.hack.stock2u.global.exception;

import com.hack.stock2u.authentication.AuthException;
import com.hack.stock2u.file.exception.FileException;
import com.hack.stock2u.user.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentTypeMismatchException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BasicException.class)
  protected ResponseEntity<BasicErrorResponse> handleBasicException(BasicException ex) {
    BasicErrorResponse res = ex.getErrorResponse();
    return ResponseEntity.status(res.httpStatus()).body(res);
  }

  @ExceptionHandler(AccessDeniedException.class)
  protected ResponseEntity<BasicErrorResponse> handleAccessDeniedException(
      AccessDeniedException ex
  ) {
    BasicException basicEx = GlobalException.FORBIDDEN.create();
    return ResponseEntity.status(basicEx.getErrorResponse().httpStatus()).body(
        basicEx.getErrorResponse()
    );
  }

  @ExceptionHandler(MissingServletRequestPartException.class)
  protected ResponseEntity<BasicErrorResponse> handleMissingServletRequestPartException(
      Exception ex
  ) {
    BasicException res = FileException.NOT_INCLUDE_FILE.create();
    return ResponseEntity.status(res.getErrorResponse().httpStatus()).body(res.getErrorResponse());
  }

  @ExceptionHandler({MethodArgumentTypeMismatchException.class, IllegalArgumentException.class,
      MissingServletRequestParameterException.class})
  protected ResponseEntity<BasicErrorResponse> handleIllegalArgsException(Exception ex) {
    String message = ex.getLocalizedMessage();
    String errorMessage = getSimpleMessage(message, IllegalArgumentException.class.getName());
    BasicErrorResponse res =
        new BasicErrorResponse("FAULT_ARG", errorMessage, HttpStatus.BAD_REQUEST);
    return ResponseEntity.status(res.httpStatus()).body(res);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  protected ResponseEntity<BasicErrorResponse> handleMethodArgNotValidException(
      MethodArgumentNotValidException ex
  ) {
    FieldError fieldError = ex.getFieldError();
    String message;
    if (fieldError != null) {
      message = fieldError.getDefaultMessage();
    } else {
      message = GlobalException.BAD_REQUEST.getMessage();
    }
    BasicErrorResponse res =
        new BasicErrorResponse("NOT_VALID_ARG", message, HttpStatus.BAD_REQUEST);
    return ResponseEntity.status(res.httpStatus()).body(res);
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  public ResponseEntity<BasicErrorResponse> handleUsernameNotFoundException() {
    BasicException ex = UserException.NOT_FOUND_USER.create();
    BasicErrorResponse res = ex.getErrorResponse();
    return ResponseEntity.status(res.httpStatus()).body(res);
  }

  private String getSimpleMessage(String fullText, String exName) {
    log.error(fullText);
    int startIdx = fullText.indexOf(exName);
    if (startIdx == -1) {
      return fullText;
    }
    return fullText.substring(startIdx + exName.length() + 2);
  }
}
