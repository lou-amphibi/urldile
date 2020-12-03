package ru.louamphibi.urldile.error.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UrlResponseStatusException extends RuntimeException {

    private int status;

    public UrlResponseStatusException(String message, int status) {
        super(message);
        this.status = status;
    }
}
