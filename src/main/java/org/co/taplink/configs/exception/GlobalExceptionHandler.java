package org.co.taplink.configs.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.co.taplink.users.modals.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

import static org.co.taplink.users.utils.TapLinkAppConstants.*;
import static org.co.taplink.users.utils.TapLinkAppMessages.Error.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Handle Login Failures (Wrong Password / Username)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleUsernameNotFoundException(BadCredentialsException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                UNAUTHORIZED,
                UNAUTHORIZED_MSG,
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    // 2. Handle Method Authorization Failures (@PreAuthorize fails)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                FORBIDDEN,
                FORBIDDEN_MSG,
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    // 3. Fallback for any other unhandled exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<@NonNull ErrorResponse> handleGlobalException(Exception ex, HttpServletRequest request) {
        // Log the actual exception in the console for debugging
        ex.printStackTrace();

        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                INTERNAL_SERVER,
                INTERNAL_SERVER_MSG,
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
