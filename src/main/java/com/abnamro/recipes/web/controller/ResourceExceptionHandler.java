package com.abnamro.recipes.web.controller;

import com.abnamro.recipes.exception.RecipeNotFoundException;
import com.abnamro.recipes.exception.UserNotFoundException;
import com.abnamro.recipes.web.dto.ProblemDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebInputException;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ResourceExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ResourceExceptionHandler.class);

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ProblemDto handleWebExchangeBindException(UserNotFoundException ex) {
        LOG.warn("UserNotFoundException", ex);
        return new ProblemDto(LocalDateTime.now(), ex.getMessage());
    }

    @ExceptionHandler(RecipeNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ProblemDto handleWebExchangeBindException(RecipeNotFoundException ex) {
        LOG.warn("RecipeNotFoundException", ex);
        return new ProblemDto(LocalDateTime.now(), ex.getMessage());
    }

    @ExceptionHandler({MissingRequestValueException.class,
            MethodArgumentNotValidException.class,
            ServerWebInputException.class,
            MissingRequestHeaderException.class})
    @ResponseStatus(BAD_REQUEST)
    public ProblemDto handleServerWebInputException(Exception ex) {
        LOG.warn("Validation Exception", ex);
        return new ProblemDto(LocalDateTime.now(), ex.getMessage());
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ProblemDto handleThrowable(Throwable t) {
        LOG.error("Unknown Exception", t);
        return new ProblemDto(LocalDateTime.now(), t.getMessage());
    }
}
