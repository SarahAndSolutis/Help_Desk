package com.helpdesk.ticket_service.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.helpdesk.ticket_service.dto.TicketRequestDTO;
import com.helpdesk.ticket_service.dto.TicketResponseDTO;
import com.helpdesk.ticket_service.dto.TicketTechnicianDTO;
import com.helpdesk.ticket_service.dto.TicketUpdateDTO;
import com.helpdesk.ticket_service.model.TicketCategory;
import com.helpdesk.ticket_service.model.TicketPriority;
import com.helpdesk.ticket_service.model.TicketStatus;
import com.helpdesk.ticket_service.service.TicketService;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService service;

    public TicketController(TicketService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TicketResponseDTO> create(@Valid @RequestBody TicketRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createTicket(dto));
    }

    @GetMapping
    public ResponseEntity<Page<TicketResponseDTO>> getAll(
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable)); 
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id)); 
    }
    
    @GetMapping("/customer/{customerId}") // Consultar chamados de determinado cliente[cite: 2]
    public ResponseEntity<Page<TicketResponseDTO>> getByCustomer(
            @PathVariable Long customerId,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(service.findByCustomerId(customerId, pageable));
    }

    @GetMapping("/search") // Pesquisar chamados[cite: 2]
    public ResponseEntity<Page<TicketResponseDTO>> searchByTitle(
            @RequestParam String title,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(service.searchByTitle(title, pageable));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<TicketResponseDTO>> getByStatus(
            @PathVariable TicketStatus status,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(service.findByStatus(status, pageable));
    }

    @GetMapping("/priority/{priority}") // Filtrar por prioridade[cite: 2]
    public ResponseEntity<Page<TicketResponseDTO>> getByPriority(
            @PathVariable TicketPriority priority,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(service.findByPriority(priority, pageable));
    }

    @GetMapping("/category/{category}") 
    public ResponseEntity<Page<TicketResponseDTO>> getByCategory(
            @PathVariable TicketCategory category,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(service.findByCategory(category, pageable));
    }

    @PutMapping("/{id}") 
    public ResponseEntity<TicketResponseDTO> update(
            @PathVariable Long id, 
            @RequestBody TicketUpdateDTO dto) {
        return ResponseEntity.ok(service.updateTicket(id, dto)); 
    }

    @PatchMapping("/{id}/assign") 
    public ResponseEntity<TicketResponseDTO> assignTechnician(
            @PathVariable Long id, 
            @Valid @RequestBody TicketTechnicianDTO dto) {
        return ResponseEntity.ok(service.assignTechnician(id, dto));
    }

    @PatchMapping("/{id}/close") 
    public ResponseEntity<TicketResponseDTO> closeTicket(@PathVariable Long id) {
        return ResponseEntity.ok(service.closeTicket(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }
}