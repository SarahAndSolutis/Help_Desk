package com.helpdesk.notification_service.exception;

import org.springframework.http.HttpStatus;

public class NotificationNotFoundException extends BaseException {
    public NotificationNotFoundException() {
        super(HttpStatus.NOT_FOUND, 
            "Notificação não encontrada", 
            "urn:problem-type:notification-not-found");
        //TODO Auto-generated constructor stub
    }
}

