package com.helpdesk.ticket_service.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<TicketResponseDTO> page = service.findAll(pageable);
        if (page.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(page); 
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id)); 
    }
    
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Page<TicketResponseDTO>> getByCustomer(
            @PathVariable Long customerId,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<TicketResponseDTO> page = service.findByCustomerId(customerId, pageable);
        if (page.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(page);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<TicketResponseDTO>> searchByTitle(
            @RequestParam String title,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<TicketResponseDTO> page = service.searchByTitle(title, pageable);
        if (page.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(page);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<TicketResponseDTO>> getByStatus(
            @PathVariable TicketStatus status,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<TicketResponseDTO> page = service.findByStatus(status, pageable);
        if (page.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(page);
    }

    @GetMapping("/priority/{priority}") 
    public ResponseEntity<Page<TicketResponseDTO>> getByPriority(
            @PathVariable TicketPriority priority,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<TicketResponseDTO> page = service.findByPriority(priority, pageable);
        if (page.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(page);
    }

    @GetMapping("/category/{category}") 
    public ResponseEntity<Page<TicketResponseDTO>> getByCategory(
            @PathVariable TicketCategory category,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<TicketResponseDTO> page = service.findByCategory(category, pageable);
        if (page.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(page);
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