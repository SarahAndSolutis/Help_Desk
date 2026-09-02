package com.helpdesk.ticket_service.dto;

public record TicketEventDTO(
    String eventType, 
    TicketResponseDTO ticket 
) {}