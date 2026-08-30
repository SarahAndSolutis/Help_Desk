package com.helpdesk.user_service.exception;

import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends BaseException {

    public EmailAlreadyExistsException (String email){
        super(HttpStatus.CONFLICT, 
            "O email " + email + " ja está sendo utilizado por outro usuario",
            "urn:problem-type:email-already-exists");
    }
    
}
