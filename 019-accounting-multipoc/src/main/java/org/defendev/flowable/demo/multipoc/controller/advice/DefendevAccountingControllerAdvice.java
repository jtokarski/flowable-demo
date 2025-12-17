package org.defendev.flowable.demo.multipoc.controller.advice;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.defendev.common.domain.error.ErrorWrapperDto;
import org.defendev.common.domain.exception.CommandFailedException;
import org.defendev.common.domain.exception.DefendevIllegalArgumentException;
import org.defendev.common.domain.exception.DefendevIllegalStateException;
import org.defendev.common.domain.exception.QueryFailedException;
import org.defendev.common.spring6.web.controlleradvice.ExceptionHandlers;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;



@ControllerAdvice
public class DefendevAccountingControllerAdvice {

    private static final Logger log = LogManager.getLogger();

    @ExceptionHandler(QueryFailedException.class)
    public ResponseEntity<ErrorWrapperDto> handleException(QueryFailedException exception) {
        return ExceptionHandlers.toResponseEntity(exception, null, MediaType.APPLICATION_JSON);
    }

    @ExceptionHandler(CommandFailedException.class)
    public ResponseEntity<ErrorWrapperDto> handleException(CommandFailedException exception) {
        return ExceptionHandlers.toResponseEntity(exception, null, MediaType.APPLICATION_JSON);
    }

    @ExceptionHandler(DefendevIllegalStateException.class)
    public ResponseEntity<ErrorWrapperDto> handleException(DefendevIllegalStateException exception) {
        return ExceptionHandlers.toResponseEntity(exception, null, MediaType.APPLICATION_JSON);
    }

    @ExceptionHandler(DefendevIllegalArgumentException.class)
    public ResponseEntity<ErrorWrapperDto> handleException(DefendevIllegalArgumentException exception) {
        return ExceptionHandlers.toResponseEntity(exception, null, MediaType.APPLICATION_JSON);
    }

}
