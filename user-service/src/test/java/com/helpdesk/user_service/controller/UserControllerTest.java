package com.helpdesk.user_service.controller;

import com.helpdesk.user_service.dto.UserRequestDTO;
import com.helpdesk.user_service.dto.UserResponseDTO;
import com.helpdesk.user_service.model.UserRole;
import com.helpdesk.user_service.service.UserService;
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
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        responseDTO = new UserResponseDTO(1L, "Sarah", "sarah@example.com", UserRole.ADMIN, true, LocalDateTime.now());
    }

    @Test
    void create_ShouldReturn201() {
        UserRequestDTO request = new UserRequestDTO("Sarah", "sarah@example.com", UserRole.ADMIN);
        when(userService.createUser(any(UserRequestDTO.class))).thenReturn(responseDTO);

        ResponseEntity<UserResponseDTO> response = userController.create(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Sarah", response.getBody().name());
    }

    @Test
    void getById_ShouldReturn200() {
        when(userService.getUserById(1L)).thenReturn(responseDTO);

        ResponseEntity<UserResponseDTO> response = userController.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Sarah", response.getBody().name());
    }

    @Test
    void getAll_ShouldReturn200AndPage() {
        Pageable pageable = PageRequest.of(0, 10);
        when(userService.getAllUsers(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(responseDTO)));

        ResponseEntity<Page<UserResponseDTO>> response = userController.list(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        assertEquals("Sarah", response.getBody().getContent().get(0).name());
    }

    @Test
    void update_ShouldReturn200() {
        com.helpdesk.user_service.dto.UserUpdateDTO updateDTO = new com.helpdesk.user_service.dto.UserUpdateDTO(
                "Sarah Atualizada", null, null);
        
        UserResponseDTO updatedResponse = new UserResponseDTO(1L, "Sarah Atualizada", "sarah@example.com", UserRole.ADMIN, true, LocalDateTime.now());
        
        when(userService.updateUser(eq(1L), any(com.helpdesk.user_service.dto.UserUpdateDTO.class))).thenReturn(updatedResponse);

        ResponseEntity<UserResponseDTO> response = userController.update(1L, updateDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Sarah Atualizada", response.getBody().name());
    }
}
