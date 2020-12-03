package ru.louamphibi.urldile.error.errorrepsonse;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DefaultInternalExceptionResponse {

    private Class exceptionClass;

    private String message;

    private StackTraceElement[] stackTrace;
}
