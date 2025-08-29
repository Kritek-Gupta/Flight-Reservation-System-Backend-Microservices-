package com.urs.user.service;

import com.urs.user.dto.LoginDto;
import com.urs.user.dto.UserDto;
import com.urs.user.entity.User;
import com.urs.user.exception.GlobalExceptionHandler;
import com.urs.user.repository.UserRepository;

import jakarta.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    ModelMapper modelMapper;

    @Override
    public String registerUser(UserDto userDto) throws GlobalExceptionHandler {
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new GlobalExceptionHandler("username.exists");
        }
        User user = new User();
        user = modelMapper.map(userDto, User.class);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);
        return "User registered successfully, Welcome " + user.getUsername();
    }

    @Override
    public String loginUser(LoginDto loginDto) throws GlobalExceptionHandler {
        User user = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new GlobalExceptionHandler(""));

        if (passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            return "Login successful";
        } else {
            throw new GlobalExceptionHandler("");
        }
    }

    @Override
    public String updateUserDetails(LoginDto loginDto, UserDto userDto) throws GlobalExceptionHandler {
        User user = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new GlobalExceptionHandler("credential.invalid"));
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new GlobalExceptionHandler("credential.invalid");
        }
        // Update fields from userDto (except username)
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getCity() != null) {
            user.setCity(userDto.getCity());
        }
        if (userDto.getContactNumber() != null) {
            user.setContactNumber(userDto.getContactNumber());
        }
        userRepository.save(user);
        return "User details updated successfully for " + user.getUsername();
    }

}