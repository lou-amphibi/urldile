package ru.louamphibi.urldile.error.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = false)
public class CustomerLinkNotFoundException extends RuntimeException {

    private HttpStatus status;

    public CustomerLinkNotFoundException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
