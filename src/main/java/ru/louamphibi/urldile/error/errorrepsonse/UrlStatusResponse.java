package ru.louamphibi.urldile.error.errorrepsonse;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class UrlStatusResponse {

    private Class exceptionClass;

    private String message;


    private int statusCode;

    private HttpStatus status;
}
