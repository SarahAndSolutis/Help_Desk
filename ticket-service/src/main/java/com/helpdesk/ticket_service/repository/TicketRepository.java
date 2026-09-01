package com.helpdesk.ticket_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.helpdesk.ticket_service.model.Ticket;
import com.helpdesk.ticket_service.model.TicketCategory;
import com.helpdesk.ticket_service.model.TicketPriority;
import com.helpdesk.ticket_service.model.TicketStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    
    Page<Ticket> findByCustomerId(Long customerId, Pageable pageable);
    
    // Filtros com paginação[cite: 2]
    Page<Ticket> findByStatus(TicketStatus status, Pageable pageable);
    Page<Ticket> findByPriority(TicketPriority priority, Pageable pageable);
    Page<Ticket> findByCategory(TicketCategory category, Pageable pageable);
    Page<Ticket> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
