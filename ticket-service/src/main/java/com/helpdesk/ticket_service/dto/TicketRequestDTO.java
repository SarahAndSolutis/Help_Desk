package com.helpdesk.ticket_service.dto;

import com.helpdesk.ticket_service.model.TicketCategory;
import com.helpdesk.ticket_service.model.TicketPriority;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TicketRequestDTO(
    @NotBlank(message = "O titulo é obrigatorio")
    String title,
    @NotBlank(message = "A descrição é obrigatoria")
    String description,
    @NotNull(message = "A prioridade é obrigatoria")
    TicketPriority priority,
    @NotNull(message = "A categoria é obrigatoria")
    TicketCategory category,
    @NotNull(message = "O Id do cliente é obrigatorio")
    Long customerId


) {
    
}
