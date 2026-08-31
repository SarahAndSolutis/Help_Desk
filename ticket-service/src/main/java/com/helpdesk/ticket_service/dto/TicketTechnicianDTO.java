package com.helpdesk.ticket_service.dto;

import jakarta.validation.constraints.NotNull;

public record TicketTechnicianDTO(
    @NotNull(message = "É necessario o ID do tecnico")
    Long technicianId
) {
    
}
