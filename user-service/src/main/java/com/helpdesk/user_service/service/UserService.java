package com.helpdesk.user_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.helpdesk.user_service.dto.UserRequestDTO;
import com.helpdesk.user_service.dto.UserResponseDTO;
import com.helpdesk.user_service.dto.UserUpdateDTO;
import com.helpdesk.user_service.exception.EmailAlreadyExistsException;
import com.helpdesk.user_service.exception.UserNotFoundException;
import com.helpdesk.user_service.model.User;
import com.helpdesk.user_service.repository.UserRepository;

import jakarta.transaction.Transactional;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Transactional
    public UserResponseDTO createUser(UserRequestDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException(dto.email());
        }

        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setRole(dto.role());

        user = userRepository.save(user);
        
        return new UserResponseDTO(user);
    }
    public Page<UserResponseDTO> getAllUsers(Pageable pageable){
        return userRepository.findAll(pageable)
                .map(UserResponseDTO::new);
    }

    public UserResponseDTO getUserById(Long id) {
        User user = findEntityById(id);
        return new UserResponseDTO(user);
    }
    public UserResponseDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException());
        
        return new UserResponseDTO(user);
    }
    @Transactional
    public UserResponseDTO updateUser(Long id, UserUpdateDTO dto) {
        User user = findEntityById(id);
        if (dto.name() != null && !dto.name().isBlank()) {
            user.setName(dto.name());
        }
        if (dto.email() != null && !dto.email().isBlank()) {
            if (!user.getEmail().equals(dto.email()) && userRepository.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException(dto.email());
            }
            user.setEmail(dto.email());
        }
        if (dto.role() != null) {
        user.setRole(dto.role());
        }
        user = userRepository.save(user);
        return new UserResponseDTO(user);
    }
    @Transactional
    public void inactivateUser(Long id) {
        User user = findEntityById(id);
        user.setActive(false); 
        userRepository.save(user);
    }
    private User findEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException());
    }
}
