package com.helpdesk.user_service.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BaseException{

    public UserNotFoundException() {
        super(HttpStatus.NOT_FOUND, 
            "Usuario não encontrado", 
            "urn:problem-type:user-not-found");
        //TODO Auto-generated constructor stub
    }
    
}
