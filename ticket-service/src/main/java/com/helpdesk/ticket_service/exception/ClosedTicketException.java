package com.helpdesk.ticket_service.exception;

import org.springframework.http.HttpStatus;

public class ClosedTicketException extends BaseException {
    public ClosedTicketException (){
        super(HttpStatus.CONFLICT, 
            "Este chamado já se encontra encerrado.",
            "urn:problem-type:closed-ticket");
    }
}
