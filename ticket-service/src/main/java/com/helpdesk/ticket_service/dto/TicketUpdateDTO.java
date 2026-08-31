package com.helpdesk.ticket_service.dto;

import com.helpdesk.ticket_service.model.*;

public record TicketUpdateDTO(
   String description,
    TicketPriority priority,
    TicketCategory category,
    TicketStatus status 
) {}
