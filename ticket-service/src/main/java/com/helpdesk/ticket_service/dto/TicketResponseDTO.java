package com.helpdesk.ticket_service.dto;

import java.time.LocalDateTime;
import com.helpdesk.ticket_service.model.Ticket;
import com.helpdesk.ticket_service.model.TicketCategory;
import com.helpdesk.ticket_service.model.TicketPriority;
import com.helpdesk.ticket_service.model.TicketStatus;

public record TicketResponseDTO(
    Long id,
    String title,
    String description,
    TicketPriority priority,
    TicketStatus status,
    TicketCategory category,
    Long customerId,
    Long technicianId, 
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public TicketResponseDTO(Ticket ticket) {
        this(
            ticket.getId(),
            ticket.getTitle(),
            ticket.getDescription(),
            ticket.getPriority(),
            ticket.getStatus(),
            ticket.getCategory(),
            ticket.getCustomerId(),
            ticket.getTechnicianId(),
            ticket.getCreatedAt(),
            ticket.getUpdatedAt()
        );
    }
}