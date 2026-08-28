package com.helpdesk.user_service.dto;

import java.time.LocalDateTime;

import com.helpdesk.user_service.model.User;
import com.helpdesk.user_service.model.UserRole;

public record UserResponseDTO(Long id, String name, String email, UserRole role, Boolean active, LocalDateTime createdAt) {
    public UserResponseDTO(User user) {
        this(user.getId(), user.getName(), user.getEmail(), user.getRole(), user.isActive(), user.getCreatedAt());
    }
}
