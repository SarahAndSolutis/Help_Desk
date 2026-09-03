package com.helpdesk.ticket_service.service;

import com.helpdesk.ticket_service.dto.*;
import com.helpdesk.ticket_service.exception.ClosedTicketException;
import com.helpdesk.ticket_service.exception.TicketNotFoundException;
import com.helpdesk.ticket_service.model.Ticket;
import com.helpdesk.ticket_service.model.TicketCategory;
import com.helpdesk.ticket_service.model.TicketPriority;
import com.helpdesk.ticket_service.model.TicketStatus;
import com.helpdesk.ticket_service.publisher.TicketEventPublisher;
import com.helpdesk.ticket_service.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository repository;

    @Mock
    private TicketEventPublisher eventPublisher;

    @InjectMocks
    private TicketService ticketService;

    private Ticket ticket;

    @BeforeEach
    void setUp() {
        ticket = new Ticket();
        ticket.setId(1L);
        ticket.setTitle("Problema no PC");
        ticket.setDescription("Não liga");
        ticket.setPriority(TicketPriority.HIGH);
        ticket.setCategory(TicketCategory.HARDWARE);
        ticket.setCustomerId(10L);
        ticket.setStatus(TicketStatus.OPEN);
    }

    @Test
    void createTicket_ShouldSaveAndPublishEvent() {
        TicketRequestDTO requestDTO = new TicketRequestDTO(
                "Problema no PC", "Não liga", TicketPriority.HIGH, TicketCategory.HARDWARE, 10L);

        when(repository.save(any(Ticket.class))).thenReturn(ticket);

        TicketResponseDTO responseDTO = ticketService.createTicket(requestDTO);

        assertNotNull(responseDTO);
        assertEquals("Problema no PC", responseDTO.title());
        verify(repository, times(1)).save(any(Ticket.class));
        verify(eventPublisher, times(1)).publishEvent(any(TicketResponseDTO.class), eq("TicketCreated"));
    }

    @Test
    void assignTechnician_ShouldUpdateStatusAndPublishEvent() {
        TicketTechnicianDTO dto = new TicketTechnicianDTO(20L);
        when(repository.findById(1L)).thenReturn(Optional.of(ticket));
        when(repository.save(any(Ticket.class))).thenReturn(ticket);

        TicketResponseDTO response = ticketService.assignTechnician(1L, dto);

        assertEquals(TicketStatus.IN_PROGRESS, response.status());
        assertEquals(20L, response.technicianId());
        verify(repository).save(ticket);
        verify(eventPublisher).publishEvent(any(TicketResponseDTO.class), eq("TicketAssigned"));
    }

    @Test
    void assignTechnician_WhenTicketIsClosed_ShouldThrowException() {
        ticket.setStatus(TicketStatus.CLOSED);
        TicketTechnicianDTO dto = new TicketTechnicianDTO(20L);
        when(repository.findById(1L)).thenReturn(Optional.of(ticket));

        assertThrows(ClosedTicketException.class, () -> ticketService.assignTechnician(1L, dto));
        verify(repository, never()).save(any(Ticket.class));
        verify(eventPublisher, never()).publishEvent(any(), anyString());
    }

    @Test
    void closeTicket_ShouldUpdateStatusAndPublishEvent() {
        when(repository.findById(1L)).thenReturn(Optional.of(ticket));
        when(repository.save(any(Ticket.class))).thenReturn(ticket);

        TicketResponseDTO response = ticketService.closeTicket(1L);

        assertEquals(TicketStatus.CLOSED, response.status());
        verify(repository).save(ticket);
        verify(eventPublisher).publishEvent(any(TicketResponseDTO.class), eq("TicketStatusChanged"));
    }

    @Test
    void closeTicket_WhenAlreadyClosed_ShouldThrowException() {
        ticket.setStatus(TicketStatus.CLOSED);
        when(repository.findById(1L)).thenReturn(Optional.of(ticket));

        assertThrows(ClosedTicketException.class, () -> ticketService.closeTicket(1L));
        verify(repository, never()).save(any(Ticket.class));
    }

    @Test
    void findById_WhenTicketNotFound_ShouldThrowException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(TicketNotFoundException.class, () -> ticketService.findById(1L));
    }
}

