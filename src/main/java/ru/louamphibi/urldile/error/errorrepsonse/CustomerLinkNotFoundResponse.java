package ru.louamphibi.urldile.error.errorrepsonse;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class CustomerLinkNotFoundResponse {

    private Class exceptionClass;

    private String exceptionMessage;

    private int statusCode;

    private HttpStatus status;
}
