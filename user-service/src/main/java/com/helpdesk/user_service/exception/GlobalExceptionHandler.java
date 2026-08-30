package com.helpdesk.user_service.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

@Override
protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, 
            HttpHeaders headers, 
            HttpStatusCode status, 
            WebRequest request) {
        
        String errorMessage = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, errorMessage);
        problem.setType(URI.create("urn:problem-type:validation-error"));
        problem.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));
        problem.setProperty("timestamp", LocalDateTime.now());

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

@ExceptionHandler(BaseException.class)
    public ProblemDetail handleBaseException(BaseException ex, WebRequest request) {
        ProblemDetail problem = ex.getBody();
        problem.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));
        return problem;
    }
@ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex, WebRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        problem.setTitle("Erro Interno do Servidor");
        problem.setType(URI.create("urn:problem-type:internal-server-error"));
        problem.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));
        problem.setProperty("timestamp", LocalDateTime.now());
        
        return problem;
    }
    
}

