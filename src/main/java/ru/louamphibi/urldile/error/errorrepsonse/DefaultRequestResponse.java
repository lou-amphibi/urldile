package ru.louamphibi.urldile.error.errorrepsonse;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class DefaultRequestResponse {

    private Class exceptionClass;

    private String exceptionMessage;

    private HttpStatus status;

    private int statusCode;
}
