package com.urs.user.service;

import com.urs.user.dto.LoginDto;
import com.urs.user.dto.UserDto;
import com.urs.user.entity.User;
import com.urs.user.exception.GlobalExceptionHandler;
import com.urs.user.repository.UserRepository;
import com.urs.user.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private UserServiceImpl userService;

    private UserDto userDto;
    private User user;
    private LoginDto loginDto;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setUsername("testuser");
        userDto.setPassword("password");

        user = new User();
        user.setUsername("testuser");
        user.setPassword("hashedPassword");

        loginDto = new LoginDto();
        loginDto.setUsername("testuser");
        loginDto.setPassword("password");
    }

    @Test
    void registerUser_Success() throws GlobalExceptionHandler {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(modelMapper.map(userDto, User.class)).thenReturn(user);
        when(passwordEncoder.encode("password")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        String result = userService.registerUser(userDto);
        assertTrue(result.contains("User registered successfully"));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUser_UsernameExists() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        GlobalExceptionHandler ex = assertThrows(GlobalExceptionHandler.class, () -> userService.registerUser(userDto));
        assertEquals("username.exists", ex.getMessage());
    }

    @Test
    void loginUser_Success() throws GlobalExceptionHandler {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "hashedPassword")).thenReturn(true);

        String result = userService.loginUser(loginDto);
        assertEquals("Login successful", result);
    }

    @Test
    void loginUser_InvalidUsername() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        GlobalExceptionHandler ex = assertThrows(GlobalExceptionHandler.class, () -> userService.loginUser(loginDto));
        assertEquals("", ex.getMessage());
    }

    @Test
    void loginUser_InvalidPassword() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "hashedPassword")).thenReturn(false);
        GlobalExceptionHandler ex = assertThrows(GlobalExceptionHandler.class, () -> userService.loginUser(loginDto));
        assertEquals("", ex.getMessage());
    }

    @Test
    void updateUserDetails_Success() throws GlobalExceptionHandler {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "hashedPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("newHashedPassword");
        UserDto updateDto = new UserDto();
        updateDto.setPassword("newPassword");
        updateDto.setName("New Name");
        updateDto.setEmail("new@email.com");
        updateDto.setCity("New City");
        updateDto.setContactNumber("1234567890");

        String result = userService.updateUserDetails(loginDto, updateDto);
        assertTrue(result.contains("User details updated successfully"));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUserDetails_InvalidUsername() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        UserDto updateDto = new UserDto();
        GlobalExceptionHandler ex = assertThrows(GlobalExceptionHandler.class, () -> userService.updateUserDetails(loginDto, updateDto));
        assertEquals("credential.invalid", ex.getMessage());
    }

    @Test
    void updateUserDetails_InvalidPassword() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "hashedPassword")).thenReturn(false);
        UserDto updateDto = new UserDto();
        GlobalExceptionHandler ex = assertThrows(GlobalExceptionHandler.class, () -> userService.updateUserDetails(loginDto, updateDto));
        assertEquals("credential.invalid", ex.getMessage());
    }
}