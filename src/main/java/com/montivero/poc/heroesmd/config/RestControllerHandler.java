package com.montivero.poc.heroesmd.config;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.ListUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.montivero.poc.heroesmd.domain.api.error.ErrorFieldResponse;
import com.montivero.poc.heroesmd.domain.api.error.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class RestControllerHandler extends ResponseEntityExceptionHandler  {

   @Override
   public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
      log.error(ex.getMessage(), ex);

      List<FieldError> fieldErrors = ex.getFieldErrors();
      List<ErrorFieldResponse> fieldResponses = ListUtils
            .emptyIfNull(fieldErrors)
            .stream()
            .map(field -> new ErrorFieldResponse(field.getField(), field.getDefaultMessage()))
            .collect(Collectors.toList());

      HttpServletRequest httpServletRequest = ((ServletWebRequest) request).getRequest();
      ErrorResponse build = ErrorResponse.builder()
                                         .date(LocalDateTime.now())
                                         .path(httpServletRequest.getServletPath())
                                         .message("Request Validation")
                                         .detail(MethodArgumentNotValidException.class.getSimpleName())
                                         .fields(fieldResponses)
                                         .build();

      return ResponseEntity.badRequest().body(build);
   }

   @ExceptionHandler({IllegalArgumentException.class })
   public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
      log.error(ex.getMessage(), ex);

      String message = ex.getMessage();
      ErrorResponse build = ErrorResponse.builder()
                                         .date(LocalDateTime.now())
                                         .message(message)
                                         .path(request.getServletPath())
                                         .detail(MethodArgumentNotValidException.class.getSimpleName())
                                         .build();

      return ResponseEntity.badRequest().body(build);
   }

   @ExceptionHandler({ SQLIntegrityConstraintViolationException.class })
   public ResponseEntity<Object> handleSqlConstraintViolation(SQLIntegrityConstraintViolationException ex, HttpServletRequest request) {
      log.error(ex.getMessage(), ex);

      ErrorResponse build = ErrorResponse.builder()
                                         .date(LocalDateTime.now())
                                         .message("Constraint violation")
                                         .path(request.getServletPath())
                                         .detail(SQLIntegrityConstraintViolationException.class.getSimpleName())
                                         .internalCode(Objects.toString(ex.getErrorCode()))
                                         .build();

      return ResponseEntity.internalServerError().body(build);
   }

}
