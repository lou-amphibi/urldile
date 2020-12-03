package ru.louamphibi.urldile.error.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = false)
public class UrldileInternalException extends RuntimeException {

    private HttpStatus status;

    public UrldileInternalException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
