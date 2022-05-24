package com.fcastro.carlisting.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Clock;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Map<HttpStatus, String> httpClientErrorExceptionMessage = new HashMap<>();

    static {
        httpClientErrorExceptionMessage.put(HttpStatus.NOT_FOUND, "Resource was not found.");
    }

    @ExceptionHandler(value = {HttpClientErrorException.class})
    protected ResponseEntity<Object> statusCodeException(HttpClientErrorException ex, HttpServletRequest request) {

        CarListingError carListingError = CarListingError.builder()
                .timestamp(Clock.systemUTC().millis())
                .status(ex.getStatusCode().value())
                .errorType(ex.getClass().getSimpleName())
                .errorMessage(httpClientErrorExceptionMessage.get(ex.getStatusCode()))
                .errorDetail(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(ex.getStatusCode()).body(carListingError);
    }

    @ExceptionHandler(value = { Exception.class })
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> generalException(final Exception ex, final HttpServletRequest request) {

        CarListingError carListingError = CarListingError.builder()
                .timestamp(Clock.systemUTC().millis())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errorType(ex.getClass().getSimpleName())
                .errorMessage("Please, contact with support.")
                .errorDetail(ex.getMessage())
                .path(request.getRequestURI().toString())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(carListingError);
    }
}
