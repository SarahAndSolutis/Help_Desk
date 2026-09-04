package com.helpdesk.user_service.service;

import com.helpdesk.user_service.dto.UserRequestDTO;
import com.helpdesk.user_service.dto.UserResponseDTO;
import com.helpdesk.user_service.exception.EmailAlreadyExistsException;
import com.helpdesk.user_service.exception.UserNotFoundException;
import com.helpdesk.user_service.model.User;
import com.helpdesk.user_service.model.UserRole;
import com.helpdesk.user_service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Sarah");
        user.setEmail("sarah@example.com");
        user.setRole(UserRole.ADMIN);
        user.setActive(true);
    }

    @Test
    void createUser_ShouldSaveSuccessfully() {
        UserRequestDTO request = new UserRequestDTO("Sarah", "sarah@example.com", UserRole.ADMIN);
        when(userRepository.existsByEmail("sarah@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO response = userService.createUser(request);

        assertNotNull(response);
        assertEquals("Sarah", response.name());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_WhenEmailExists_ShouldThrowException() {
        UserRequestDTO request = new UserRequestDTO("Sarah", "sarah@example.com", UserRole.ADMIN);
        when(userRepository.existsByEmail("sarah@example.com")).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.createUser(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void inactivateUser_ShouldSetInactive() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.inactivateUser(1L);

        assertFalse(user.isActive());
        verify(userRepository).save(user);
    }

    @Test
    void getUserById_WhenNotFound_ShouldThrowException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(99L));
    }

    @Test
    void updateUser_ShouldUpdateFields() {
        com.helpdesk.user_service.dto.UserUpdateDTO updateDTO = new com.helpdesk.user_service.dto.UserUpdateDTO(
                "Novo Nome", "novoemail@example.com", UserRole.CLIENT);
                
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail("novoemail@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO response = userService.updateUser(1L, updateDTO);

        assertEquals("Novo Nome", response.name());
        assertEquals("novoemail@example.com", response.email());
        assertEquals(UserRole.CLIENT, response.role());
        
        verify(userRepository).save(user);
    }
}

