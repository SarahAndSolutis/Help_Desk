package com.helpdesk.ticket_service.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

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

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, "Um ou mais campos estão inválidos. Verifique os detalhes e tente novamente.");
        problem.setType(URI.create("urn:problem-type:validation-error"));
        problem.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));
        problem.setProperty("timestamp", LocalDateTime.now());
        List<CampoInvalido> erros = ex.getBindingResult().getFieldErrors().stream()
                .map(erro -> new CampoInvalido(erro.getField(), erro.getDefaultMessage()))
                .toList();
        problem.setProperty("erros", erros);

        return ResponseEntity.badRequest().body(problem);
    }

@ExceptionHandler(BaseException.class)
    public ProblemDetail handleBaseException(BaseException ex, WebRequest request) {
        ProblemDetail problem = ex.getBody();
        problem.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));
        return problem;
    }
@ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex, WebRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro interno inesperado no servidor.");
        problem.setTitle("Erro Interno do Servidor");
        problem.setType(URI.create("urn:problem-type:internal-server-error"));
        problem.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));
        problem.setProperty("timestamp", LocalDateTime.now());
        
        return problem;
    }
    private record CampoInvalido(String campo, String mensagem) {}
    
}

