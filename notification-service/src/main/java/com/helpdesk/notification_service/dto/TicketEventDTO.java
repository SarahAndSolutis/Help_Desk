package com.helpdesk.notification_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TicketEventDTO(
        @NotBlank(message = "O tipo do evento é obrigatório")
        String eventType,  
        @Valid
        @NotNull(message = "Os dados do ticket são obrigatórios")
        TicketResponseDTO ticket
) {}
