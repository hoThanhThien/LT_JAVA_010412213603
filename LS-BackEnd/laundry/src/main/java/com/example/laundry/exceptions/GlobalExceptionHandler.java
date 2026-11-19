package com.example.laundry.exceptions;

import com.example.laundry.utils.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<ApiResponse<Object>> handleResponseStatusException(ResponseStatusException ex) {
    String message = ex.getReason() != null && !ex.getReason().isBlank()
            ? ex.getReason()
            : ex.getStatusCode().toString();
    log.warn("[requestId={}] Request failed with status {}: {}", org.slf4j.MDC.get("requestId"), ex.getStatusCode(), message);
    return ResponseEntity.status(ex.getStatusCode()).body(new ApiResponse<>(message));
  }

  @ExceptionHandler(org.springframework.web.servlet.resource.NoResourceFoundException.class)
  public ResponseEntity<ApiResponse<Object>> handleNotFoundException(
          org.springframework.web.servlet.resource.NoResourceFoundException ex
  ) {
    log.warn("[requestId={}] Resource not found: {}", org.slf4j.MDC.get("requestId"), ex.getResourcePath());
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse<>("Không tìm thấy tài nguyên yêu cầu"));
  }

  @ExceptionHandler({AccessDeniedException.class, AuthorizationDeniedException.class})
  public ResponseEntity<ApiResponse<Object>> handleAccessDeniedException(Exception ex) {
    log.warn("[requestId={}] Access denied: {}", org.slf4j.MDC.get("requestId"), ex.getMessage());
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(new ApiResponse<>("Không có quyền truy cập tài nguyên này"));
  }

  @ExceptionHandler({
          IllegalArgumentException.class,
          MissingServletRequestParameterException.class,
          MethodArgumentTypeMismatchException.class,
          HttpMessageNotReadableException.class
  })
  public ResponseEntity<ApiResponse<Object>> handleBadRequestExceptions(Exception ex) {
    String message = ex.getMessage() != null && !ex.getMessage().isBlank()
            ? ex.getMessage()
            : "Yêu cầu không hợp lệ";
    log.warn("[requestId={}] Bad request: {}", org.slf4j.MDC.get("requestId"), message);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(message));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Object>> handleValidationException(MethodArgumentNotValidException ex) {
    String message = ex.getBindingResult().getFieldErrors().stream()
            .findFirst()
            .map(err -> err.getField() + ": " + err.getDefaultMessage())
            .orElse("Dữ liệu đầu vào không hợp lệ");
    log.warn("[requestId={}] Validation failed: {}", org.slf4j.MDC.get("requestId"), message);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(message));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Object>> handleUnhandledException(Exception ex) {
    log.error("[requestId={}] Unhandled exception processed by GlobalExceptionHandler", org.slf4j.MDC.get("requestId"), ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ApiResponse<>("Đã xảy ra lỗi hệ thống"));
  }
}

