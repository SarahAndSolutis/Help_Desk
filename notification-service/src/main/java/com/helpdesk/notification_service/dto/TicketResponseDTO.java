package com.helpdesk.notification_service.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TicketResponseDTO(
    @NotNull(message = "Ticket ID é obrigatório")
    Long id,    
    @NotBlank(message = "Título é obrigatório")
    String title,
        
    String description,
    String priority,
    String status,
    String category,
    Long customerId,
    Long technicianId,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    
}
