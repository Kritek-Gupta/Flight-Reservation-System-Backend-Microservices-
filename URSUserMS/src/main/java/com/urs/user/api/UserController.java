package com.urs.user.api;

import com.urs.user.dto.LoginDto;
import com.urs.user.dto.UserDto;
import com.urs.user.exception.GlobalExceptionHandler;
import com.urs.user.service.UserServiceImpl;
import com.urs.user.util.JwtUtil;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {
	
	@Autowired
	Environment env;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDto userDto, BindingResult bindingResult) throws GlobalExceptionHandler{
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        String registeredUser = userService.registerUser(userDto);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @CircuitBreaker(name = "loginUser", fallbackMethod = "loginUserFallback")
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginDto loginDto) throws GlobalExceptionHandler {
    	String response = userService.loginUser(loginDto);
        final String token = jwtUtil.generateToken(loginDto.getUsername());
        return ResponseEntity.ok("Login successful.");
    }
    
    public ResponseEntity<?> loginUserFallback(LoginDto loginDto, Throwable e) {
		return new ResponseEntity<>("Service is currently unavailable or Invalid Credentials. Please try again later. " + e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
	}
    
    @PutMapping("/update-details")
    public ResponseEntity<?> updateUserDetails(@Valid @RequestBody UserDto userDto,
                                               BindingResult bindingResult) throws GlobalExceptionHandler {
    	if (bindingResult.hasErrors()) {
            // Handle validation errors
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername(userDto.getUsername());
        loginDto.setPassword(userDto.getPassword());
        String updatedUser = userService.updateUserDetails(loginDto, userDto);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
}