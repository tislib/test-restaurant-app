package net.tislib.restaurantapp.exception;

import lombok.extern.log4j.Log4j2;
import net.tislib.restaurantapp.data.authentication.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleException(AuthenticationException exception) {
        ErrorResponse response = new ErrorResponse("AuthenticationException", exception.getMessage());

        log.info(exception.getMessage(), exception);

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        ErrorResponse response = new ErrorResponse(exception.getClass().getSimpleName(), exception.getMessage());

        log.error(exception.getMessage(), exception);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        ErrorResponse response = new ErrorResponse(exception.getClass().getSimpleName(), exception.getMessage());

        log.error(exception.getMessage(), exception);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
