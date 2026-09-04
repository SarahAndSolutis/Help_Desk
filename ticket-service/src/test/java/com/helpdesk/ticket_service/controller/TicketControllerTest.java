package com.helpdesk.ticket_service.controller;

import com.helpdesk.ticket_service.dto.TicketRequestDTO;
import com.helpdesk.ticket_service.dto.TicketResponseDTO;
import com.helpdesk.ticket_service.model.TicketCategory;
import com.helpdesk.ticket_service.model.TicketPriority;
import com.helpdesk.ticket_service.model.TicketStatus;
import com.helpdesk.ticket_service.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketControllerTest {

    @Mock
    private TicketService ticketService;

    @InjectMocks
    private TicketController ticketController;

    private TicketResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        responseDTO = new TicketResponseDTO(
                1L, "Problema no PC", "Não liga",
                TicketPriority.HIGH, TicketStatus.OPEN, TicketCategory.HARDWARE,
                10L, null, LocalDateTime.now(), null
        );
    }

    @Test
    void createTicket_ShouldReturn201() {
        TicketRequestDTO requestDTO = new TicketRequestDTO(
                "Problema no PC", "Não liga", TicketPriority.HIGH, TicketCategory.HARDWARE, 10L);

        when(ticketService.createTicket(any(TicketRequestDTO.class))).thenReturn(responseDTO);

        ResponseEntity<TicketResponseDTO> response = ticketController.create(requestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Problema no PC", response.getBody().title());
    }

    @Test
    void getById_ShouldReturn200() {
        when(ticketService.findById(1L)).thenReturn(responseDTO);

        ResponseEntity<TicketResponseDTO> response = ticketController.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Problema no PC", response.getBody().title());
    }

    @Test
    void getAll_ShouldReturn200AndPage() {
        Pageable pageable = PageRequest.of(0, 10);
        when(ticketService.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(responseDTO)));

        ResponseEntity<Page<TicketResponseDTO>> response = ticketController.getAll(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        assertEquals("Problema no PC", response.getBody().getContent().get(0).title());
    }

    @Test
    void assignTechnician_ShouldReturn200() {
        com.helpdesk.ticket_service.dto.TicketTechnicianDTO technicianDTO = new com.helpdesk.ticket_service.dto.TicketTechnicianDTO(20L);
        TicketResponseDTO updatedResponse = new TicketResponseDTO(
                1L, "Problema no PC", "Não liga",
                TicketPriority.HIGH, TicketStatus.IN_PROGRESS, TicketCategory.HARDWARE,
                10L, 20L, LocalDateTime.now(), LocalDateTime.now()
        );
        
        when(ticketService.assignTechnician(eq(1L), any(com.helpdesk.ticket_service.dto.TicketTechnicianDTO.class))).thenReturn(updatedResponse);

        ResponseEntity<TicketResponseDTO> response = ticketController.assignTechnician(1L, technicianDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(TicketStatus.IN_PROGRESS, response.getBody().status());
        assertEquals(20L, response.getBody().technicianId());
    }

    @Test
    void close_ShouldReturn200() {
        TicketResponseDTO closedResponse = new TicketResponseDTO(
                1L, "Problema no PC", "Não liga",
                TicketPriority.HIGH, TicketStatus.CLOSED, TicketCategory.HARDWARE,
                10L, 20L, LocalDateTime.now(), LocalDateTime.now()
        );
        
        when(ticketService.closeTicket(1L)).thenReturn(closedResponse);

        ResponseEntity<TicketResponseDTO> response = ticketController.closeTicket(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(TicketStatus.CLOSED, response.getBody().status());
    }
}
