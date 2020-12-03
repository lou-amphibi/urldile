package ru.louamphibi.urldile.error.errorrepsonse;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class UnknownHostResponse {

    private Class exceptionClass;

    private String host;

    private String message;

    private HttpStatus status;

    private int statusCode;
}
