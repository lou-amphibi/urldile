package ru.louamphibi.urldile.error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.louamphibi.urldile.error.errorrepsonse.*;
import ru.louamphibi.urldile.error.exception.CustomerLinkNotFoundException;
import ru.louamphibi.urldile.error.exception.UrlResponseStatusException;
import ru.louamphibi.urldile.error.exception.UrldileInternalException;
import java.net.UnknownHostException;

@ControllerAdvice
public class UrldileExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        JsonParseResponse jsonParseResponse = new JsonParseResponse(ex.getClass(), ex.getMessage(),
                "Malformed JSON request", status.value(), status);

        return new ResponseEntity<>(jsonParseResponse, status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        String rejectValue;
        String validationField = "url";
        String rejectedNullValue = "null";

        try {
            rejectValue = ex.getBindingResult().getFieldError().getRejectedValue().toString();
        } catch (NullPointerException e) {
            rejectValue = rejectedNullValue;
        }

        ApiValidResponse apiValidResponse = new ApiValidResponse(ex.getClass(), ex.getMessage(),
                validationField, rejectValue,
                ex.getBindingResult().getFieldError().getDefaultMessage(),
                status.value(), status);

        return new ResponseEntity<>(apiValidResponse, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                 HttpHeaders headers, HttpStatus status, WebRequest request) {
        DefaultRequestResponse defaultRequestResponse = new DefaultRequestResponse(ex.getClass(), ex.getMessage(),
                status, status.value());

        return new ResponseEntity<>(defaultRequestResponse, status);
    }

    @ExceptionHandler
    public ResponseEntity handleException(UrlResponseStatusException ex) {
        UrlStatusResponse urlStatusResponse = new UrlStatusResponse(ex.getClass(), ex.getMessage(),
                 ex.getStatus(), HttpStatus.resolve(ex.getStatus()));

        return new ResponseEntity<>(urlStatusResponse, urlStatusResponse.getStatus());
    }

    @ExceptionHandler
    public ResponseEntity handleException(UnknownHostException ex) {
        UnknownHostResponse unknownHostResponse = new UnknownHostResponse(ex.getClass(), ex.getMessage(),
                "host with this address not found", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(unknownHostResponse, unknownHostResponse.getStatus());
    }

    @ExceptionHandler
    public ResponseEntity handleException(Throwable ex) {
        DefaultInternalExceptionResponse defaultInternalExceptionResponse = new DefaultInternalExceptionResponse(ex.getClass(), ex.getMessage(), ex.getStackTrace());

        return new ResponseEntity<>(defaultInternalExceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity handleException(UrldileInternalException ex) {
        UrldileInternalResponse urldileInternalResponse = new UrldileInternalResponse(ex.getClass(), ex.getMessage(),
                ex.getStatus().value(), ex.getStatus());

        return new ResponseEntity<>(urldileInternalResponse, urldileInternalResponse.getStatus());
    }

    @ExceptionHandler
    public ResponseEntity handleException(CustomerLinkNotFoundException ex) {
        CustomerLinkNotFoundResponse customerLinkNotFoundResponse = new CustomerLinkNotFoundResponse(ex.getClass(),
                ex.getMessage(), ex.getStatus().value(), ex.getStatus());

        return new ResponseEntity<>(customerLinkNotFoundResponse, customerLinkNotFoundResponse.getStatus());
    }
}
