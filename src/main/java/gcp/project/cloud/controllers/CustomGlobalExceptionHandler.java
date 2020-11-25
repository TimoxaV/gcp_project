package gcp.project.cloud.controllers;

import gcp.project.cloud.exceptions.ApiError;
import gcp.project.cloud.exceptions.DataProcessException;
import gcp.project.cloud.exceptions.JobWaitingException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.nio.file.NoSuchFileException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        List<String> errors = exception.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        body.put("errors", errors);
        return new ResponseEntity<>(body, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException exception,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        String error = "Notification's JSON request is not in appropriate form.";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, exception));
    }

    @ExceptionHandler(NoSuchFileException.class)
    protected ResponseEntity<Object> handleIOException(NoSuchFileException exception) {
        String message = exception.getFile() + " file is absent. Check whether all needed "
                + "files are present";
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
        apiError.setMessage(message);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(DataProcessException.class)
    protected ResponseEntity<Object> handleDataProcessException(DataProcessException exception) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
        apiError.setMessage(exception.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(JobWaitingException.class)
    protected ResponseEntity<Object> handleJobWaitingException(DataProcessException exception) {
        ApiError apiError = new ApiError(HttpStatus.REQUEST_TIMEOUT);
        apiError.setMessage(exception.getMessage());
        return buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
