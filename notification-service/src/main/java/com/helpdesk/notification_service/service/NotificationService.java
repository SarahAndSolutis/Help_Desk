package com.helpdesk.notification_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.helpdesk.notification_service.dto.NotificationResponseDTO;
import com.helpdesk.notification_service.model.Notification;
import com.helpdesk.notification_service.repository.NotificationRepository;
import com.helpdesk.notification_service.exception.NotificationNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repository;

    public List<NotificationResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(NotificationResponseDTO::new)
                .collect(Collectors.toList());
    }

    public NotificationResponseDTO findById(Long id) {
        Notification notification = repository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException()); 
                
        return new NotificationResponseDTO(notification);
    }
}