package com.helpdesk.user_service.dto;

import com.helpdesk.user_service.model.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;



public record UserRequestDTO(
    @NotBlank(message = "O nome é obrigatório")
    String name, 
    @NotBlank(message = "O email é obrigatório")
    @Email(message = "O e-mail deve ser válido")
    String email, 
    @NotNull(message = "O tipo de perfil é obrigatório")
    UserRole role) {
 
}
