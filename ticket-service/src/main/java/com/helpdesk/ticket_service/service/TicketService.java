package com.helpdesk.ticket_service.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.helpdesk.ticket_service.dto.*;
import com.helpdesk.ticket_service.exception.NoContentException;
import com.helpdesk.ticket_service.exception.UserNotFoundException;
import com.helpdesk.ticket_service.model.Ticket;
import com.helpdesk.ticket_service.model.TicketCategory;
import com.helpdesk.ticket_service.model.TicketPriority;
import com.helpdesk.ticket_service.model.TicketStatus;
import com.helpdesk.ticket_service.repository.TicketRepository;


@Service
public class TicketService {
    @Autowired
    private TicketRepository repository;

    public TicketService(TicketRepository repository) {
        this.repository = repository;
    }
    @Transactional
    public TicketResponseDTO createTicket(TicketRequestDTO dto) {
        Ticket ticket = new Ticket();
        ticket.setTitle(dto.title());
        ticket.setDescription(dto.description());
        ticket.setPriority(dto.priority());
        ticket.setCategory(dto.category());
        ticket.setCustomerId(dto.customerId());
        
        ticket = repository.save(ticket);
        
        return new TicketResponseDTO(ticket);
    }

    public Page<TicketResponseDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(TicketResponseDTO::new);
    }

    public TicketResponseDTO findById(Long id) {
        Ticket ticket = getTicketEntityById(id);
        return new TicketResponseDTO(ticket);
    }
    
    public Page<TicketResponseDTO> findByCustomerId(Long customerId,Pageable pageable) {
        return repository.findByCustomerId(customerId, pageable).map(TicketResponseDTO::new);
    }
    public Page<TicketResponseDTO> findByStatus(TicketStatus status, Pageable pageable) {
        return repository.findByStatus(status, pageable).map(TicketResponseDTO::new);
    }
    public Page<TicketResponseDTO> findByPriority(TicketPriority priority, Pageable pageable) {
        return repository.findByPriority(priority, pageable).map(TicketResponseDTO::new);
    }
    public Page<TicketResponseDTO> findByCategory(TicketCategory category, Pageable pageable) {
        return repository.findByCategory(category, pageable).map(TicketResponseDTO::new);
    }
    public Page<TicketResponseDTO> searchByTitle(String title, Pageable pageable) {
        return repository.findByTitleContainingIgnoreCase(title, pageable).map(TicketResponseDTO::new);
    }
    @Transactional
    public TicketResponseDTO updateTicket(Long id, TicketUpdateDTO dto) {
        Ticket ticket = getTicketEntityById(id);
        
        if (dto.description() != null) ticket.setDescription(dto.description()); 
        if (dto.priority() != null) ticket.setPriority(dto.priority()); 
        if (dto.category() != null) ticket.setCategory(dto.category()); 
        if (dto.status() != null) {
            ticket.setStatus(dto.status());
        }

        return new TicketResponseDTO(repository.save(ticket));
    }
    @Transactional
    public TicketResponseDTO assignTechnician(Long id, TicketTechnicianDTO dto){
        Ticket ticket = getTicketEntityById(id);
        ticket.setTechnicianId(id);
        return new TicketResponseDTO(repository.save(ticket));
    }
    @Transactional
    public TicketResponseDTO closeTicket(Long id) {
        Ticket ticket = getTicketEntityById(id);
        if (ticket.getStatus() == TicketStatus.CLOSED) {
            throw new RuntimeException("Este chamado já se encontra encerrado.");
        }

        ticket.setStatus(TicketStatus.CLOSED); 
        
        return new TicketResponseDTO(repository.save(ticket));
    }
    @Transactional
    public void deleteTicket(Long id) {
        repository.deleteById(id);
    }
    private Ticket getTicketEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException());
    }
}
