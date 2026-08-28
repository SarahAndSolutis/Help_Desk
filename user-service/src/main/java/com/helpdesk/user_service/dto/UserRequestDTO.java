package com.helpdesk.user_service.dto;

import com.helpdesk.user_service.model.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;



public record UserRequestDTO(
    @NotNull(message = "O nome é obrigatório")
    String name, 
    @NotNull(message = "O e-mail é obrigatório")
    @Email(message = "O e-mail deve ser válido")
    String email, 
    @NotNull(message = "O tipo de perfil é obrigatório")
    UserRole role) {
 
}
