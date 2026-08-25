package com.zencart.user_service.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    public record ErrorResponse(Instant timestamp, int status, String error, String message, String path) {}

    @ExceptionHandler(UserObjectNotFoundException.class)
    ResponseEntity<ErrorResponse> userNotFound(UserObjectNotFoundException ex, HttpServletRequest r) {
        return error(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", ex.getMessage(), r);
    }

    @ExceptionHandler(AddressNotFoundException.class)
    ResponseEntity<ErrorResponse> addressNotFound(AddressNotFoundException ex, HttpServletRequest r) {
        return error(HttpStatus.NOT_FOUND, "ADDRESS_NOT_FOUND", ex.getMessage(), r);
    }

    @ExceptionHandler(CredentialNotFoundException.class)
    ResponseEntity<ErrorResponse> credentialNotFound(CredentialNotFoundException ex, HttpServletRequest r) {
        return error(HttpStatus.NOT_FOUND, "CREDENTIAL_NOT_FOUND", ex.getMessage(), r);
    }

    @ExceptionHandler(VerificationTokenNotFoundException.class)
    ResponseEntity<ErrorResponse> verificationNotFound(VerificationTokenNotFoundException ex, HttpServletRequest r) {
        return error(HttpStatus.NOT_FOUND, "TOKEN_NOT_FOUND", ex.getMessage(), r);
    }

    @ExceptionHandler(ConflictException.class)
    ResponseEntity<ErrorResponse> conflict(ConflictException ex, HttpServletRequest r) {
        return error(HttpStatus.CONFLICT, "CONFLICT", ex.getMessage(), r);
    }

    @ExceptionHandler({BadRequestException.class, IllegalArgumentException.class, ConstraintViolationException.class})
    ResponseEntity<ErrorResponse> badRequest(Exception ex, HttpServletRequest r) {
        return error(HttpStatus.BAD_REQUEST, "BAD_REQUEST", ex.getMessage(), r);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse> validation(MethodArgumentNotValidException ex, HttpServletRequest r) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return error(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", message, r);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    ResponseEntity<ErrorResponse> malformedRequest(Exception ex, HttpServletRequest r) {
        return error(HttpStatus.BAD_REQUEST, "MALFORMED_REQUEST", "Request contains an invalid value", r);
    }

    @ExceptionHandler(InvalidTokenException.class)
    ResponseEntity<ErrorResponse> invalidToken(InvalidTokenException ex, HttpServletRequest r) {
        return error(HttpStatus.BAD_REQUEST, "INVALID_TOKEN", ex.getMessage(), r);
    }

    @ExceptionHandler(UnauthorizedException.class)
    ResponseEntity<ErrorResponse> unauthorized(UnauthorizedException ex, HttpServletRequest r) {
        return error(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", ex.getMessage(), r);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<ErrorResponse> integrity(DataIntegrityViolationException ex, HttpServletRequest r) {
        return error(HttpStatus.CONFLICT, "DATA_INTEGRITY_VIOLATION", "Resource conflicts with existing data", r);
    }

    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<ErrorResponse> accessDenied(AccessDeniedException ex, HttpServletRequest r){
        return error(HttpStatus.FORBIDDEN, "FORBIDDEN", "You do not have permission to access this resource", r);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    ResponseEntity<ErrorResponse> authorizationDenied(AuthorizationDeniedException  ex, HttpServletRequest r){
        return error(HttpStatus.FORBIDDEN, "FORBIDDEN", "You do not have permission to access this resource", r);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponse> unexpected(Exception ex, HttpServletRequest r) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "Internal server error", r);
    }

    private ResponseEntity<ErrorResponse> error(HttpStatus status, String code, String message, HttpServletRequest request) {
        return ResponseEntity.status(status)
                .body(new ErrorResponse(Instant.now(), status.value(), code, message, request.getRequestURI()));
    }
}
