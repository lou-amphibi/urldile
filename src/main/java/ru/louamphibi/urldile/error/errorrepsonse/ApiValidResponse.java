package ru.louamphibi.urldile.error.errorrepsonse;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ApiValidResponse {

    private Class exceptionClass;

    private String exceptionMessage;

    private String field;

    private String rejectValue;

    private String message;

    private int statusCode;

    private HttpStatus status;
}
