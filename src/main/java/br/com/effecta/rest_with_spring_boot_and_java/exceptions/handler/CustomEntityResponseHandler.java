package br.com.effecta.rest_with_spring_boot_and_java.exceptions.handler;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.effecta.rest_with_spring_boot_and_java.exceptions.BadRequestException;
import br.com.effecta.rest_with_spring_boot_and_java.exceptions.ExceptionResponse;
import br.com.effecta.rest_with_spring_boot_and_java.exceptions.FileNotFoundException;
import br.com.effecta.rest_with_spring_boot_and_java.exceptions.FileStorageException;
import br.com.effecta.rest_with_spring_boot_and_java.exceptions.RequiredObjectIsNullException;
import br.com.effecta.rest_with_spring_boot_and_java.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class CustomEntityResponseHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception e, HttpServletRequest request) {
        return buildResponse(e, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleResourceNotFoundException(Exception e,
            HttpServletRequest request) {
        return buildResponse(e, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RequiredObjectIsNullException.class)
    public final ResponseEntity<ExceptionResponse> handleRequiredObjectIsNullException(Exception e,
            HttpServletRequest request) {
        return buildResponse(e, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleFileNotFoundException(Exception e,
            HttpServletRequest request) {
        return buildResponse(e, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<ExceptionResponse> handleBadRequestException(Exception e,
            HttpServletRequest request) {
        return buildResponse(e, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileStorageException.class)
    public final ResponseEntity<ExceptionResponse> handleFileStorageException(Exception e,
            HttpServletRequest request) {
        return buildResponse(e, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ExceptionResponse> buildResponse(Exception e, HttpServletRequest request,
            HttpStatus status) {
        ExceptionResponse response = new ExceptionResponse(
                new Date(),
                e.getMessage(),
                status.value(),
                status.getReasonPhrase(),
                request.getRequestURI());
        return ResponseEntity.status(status).body(response);
    }

}
