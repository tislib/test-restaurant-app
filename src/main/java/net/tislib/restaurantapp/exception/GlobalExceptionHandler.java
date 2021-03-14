package net.tislib.restaurantapp.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.jsonwebtoken.JwtException;
import lombok.extern.log4j.Log4j2;
import net.tislib.restaurantapp.data.authentication.ErrorResponse;
import net.tislib.restaurantapp.data.authentication.ErrorResponse.FieldError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException exception) {
        ErrorResponse response = new ErrorResponse(exception.getMessage());

        log.warn(exception.getMessage());

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    // we handle both AuthenticationException and JwtException as authentication problem
    @ExceptionHandler({AuthenticationException.class, JwtException.class})
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException exception) {
        ErrorResponse response = new ErrorResponse(exception.getMessage());

        log.debug(exception.getMessage());

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        ErrorResponse response = new ErrorResponse(exception.getMessage());

        log.error(exception.getMessage(), exception);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException exception) {
        ErrorResponse response = new ErrorResponse(exception.getMessage());

        log.debug(exception.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        ErrorResponse response;

        // in case of InvalidFormatException we need to return field name to show which field has formatting problem
        if (exception.getCause() instanceof InvalidFormatException) {
            InvalidFormatException cause = (InvalidFormatException) exception.getCause();
            response = new ErrorResponse("json parsing error", Collections.singleton(
                    FieldError.builder()
                            .name(cause.getPath()
                                    .stream()
                                    .map(JsonMappingException.Reference::getFieldName)
                                    .collect(Collectors.joining(".")))
                            .message(cause.getMessage())
                            .build()
            ));
        } else {
            response = new ErrorResponse(exception.getMessage());
        }

        log.warn(exception.getMessage(), exception);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception) {
        ErrorResponse response = new ErrorResponse(exception.getMessage());

        log.warn(exception.getMessage(), exception);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidFieldException.class)
    public ResponseEntity<ErrorResponse> handleInvalidFieldException(InvalidFieldException exception) {
        ErrorResponse response = new ErrorResponse(exception.getMessage(), Collections.singleton(
                FieldError.builder()
                        .name(exception.getFieldName())
                        .message(exception.getMessage())
                        .build()
        ));

        log.warn(exception.getMessage(), exception);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Set<String> rejectedFieldNames = new HashSet<>();
        Set<FieldError> rejectedFields = new HashSet<>();

        // in case of MethodArgumentNotValidException, collect and return invalid fields
        exception.getBindingResult()
                .getFieldErrors()
                .forEach(item -> {
                    rejectedFieldNames.add(item.getField());

                    rejectedFields.add(FieldError.builder()
                            .name(item.getField())
                            .message(item.getDefaultMessage())
                            .build());
                });

        ErrorResponse response = new ErrorResponse("some fields are rejected: " + rejectedFieldNames, rejectedFields);

        log.warn(exception.getMessage() + ": {}", rejectedFieldNames);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
