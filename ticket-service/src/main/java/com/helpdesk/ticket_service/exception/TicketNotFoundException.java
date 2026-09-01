package com.helpdesk.ticket_service.exception;

import org.springframework.http.HttpStatus;

public class TicketNotFoundException extends BaseException{

    public TicketNotFoundException() {
        super(HttpStatus.NOT_FOUND, 
            "Ticket não encontrado", 
            "urn:problem-type:ticket-not-found");
        //TODO Auto-generated constructor stub
    }
    
}
