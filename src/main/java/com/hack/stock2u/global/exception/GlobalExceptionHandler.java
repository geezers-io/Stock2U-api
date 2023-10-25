package com.hack.stock2u.global.exception;

import com.hack.stock2u.file.exception.FileException;
import com.hack.stock2u.user.UserException;
import javax.validation.ConstraintViolationException;
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
    ex.printStackTrace();
    BasicErrorResponse res = ex.getErrorResponse();
    return ResponseEntity.status(res.httpStatus()).body(res);
  }

  @ExceptionHandler(AccessDeniedException.class)
  protected ResponseEntity<BasicErrorResponse> handleAccessDeniedException(
      AccessDeniedException ex
  ) {
    ex.printStackTrace();
    BasicException basicEx = GlobalException.FORBIDDEN.create();
    return ResponseEntity.status(basicEx.getErrorResponse().httpStatus()).body(
        basicEx.getErrorResponse()
    );
  }

  @ExceptionHandler(MissingServletRequestPartException.class)
  protected ResponseEntity<BasicErrorResponse> handleMissingServletRequestPartException(
      Exception ex
  ) {
    ex.printStackTrace();
    BasicException res = FileException.NOT_INCLUDE_FILE.create();
    return ResponseEntity.status(res.getErrorResponse().httpStatus()).body(res.getErrorResponse());
  }

  @ExceptionHandler(ConstraintViolationException.class)
  protected ResponseEntity<BasicErrorResponse> handleConstraintViolationException(
      ConstraintViolationException ex
  ) {
    ex.printStackTrace();
    String message = ex.getMessage();
    String fieldMessage = getFieldMessage(message, ":");
    BasicErrorResponse res =
        new BasicErrorResponse("PARAM_VIOLATION", fieldMessage, HttpStatus.BAD_REQUEST);
    return ResponseEntity.status(res.httpStatus()).body(res);
  }

  @ExceptionHandler({MethodArgumentTypeMismatchException.class, IllegalArgumentException.class,
      MissingServletRequestParameterException.class})
  protected ResponseEntity<BasicErrorResponse> handleIllegalArgsException(Exception ex) {
    ex.printStackTrace();
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
    ex.printStackTrace();
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

  private String getFieldMessage(String message, String delimeter) {
    int startIdx = message.indexOf(delimeter);
    if (startIdx == -1) {
      return message;
    }
    return message.substring(startIdx + 2);
  }
}
