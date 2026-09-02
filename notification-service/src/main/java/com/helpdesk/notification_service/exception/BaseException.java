package com.helpdesk.notification_service.exception;

import java.net.URI;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;

public abstract class BaseException extends RuntimeException implements ErrorResponse {
    private final HttpStatus status;
    private final ProblemDetail problemDetail;

    protected BaseException (HttpStatus status,String menssage, String type){
        super(menssage);
        this.status = status;

        this.problemDetail = ProblemDetail.forStatusAndDetail(this.status, this.getMessage());
        this.problemDetail.setType(URI.create(type));
        this.problemDetail.setTitle(this.status.getReasonPhrase());
        this.problemDetail.setProperty("timestamp", LocalDateTime.now());
    }
    @Override
    public HttpStatus getStatusCode() {
        return this.status;
    }

    @Override
    public ProblemDetail getBody() {
        return this.problemDetail;
    }
    
}
