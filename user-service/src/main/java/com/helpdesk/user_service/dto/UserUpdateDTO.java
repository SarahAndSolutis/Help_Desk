package com.helpdesk.user_service.dto;

import com.helpdesk.user_service.model.UserRole;

import jakarta.validation.constraints.Email;

public record UserUpdateDTO(
    String name, 
    @Email(message ="O Email deve ser valido")
    String email, 
    UserRole role) {
    
}
