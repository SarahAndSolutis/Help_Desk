package com.helpdesk.notification_service.dto;

import java.time.LocalDateTime;

import com.helpdesk.notification_service.model.Notification;

public record NotificationResponseDTO(
        Long id,
        Long ticketId,
        String message,
        LocalDateTime createdAt
) {
    public NotificationResponseDTO(Notification notification) {
        this(
            notification.getId(),
            notification.getTicketId(),
            notification.getMessage(),
            notification.getCreatedAt()
        );
    }
}