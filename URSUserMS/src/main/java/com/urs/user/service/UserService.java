package com.urs.user.service;

import com.urs.user.dto.LoginDto;
import com.urs.user.dto.UserDto;
import com.urs.user.exception.GlobalExceptionHandler;

public interface UserService {
	 public String registerUser(UserDto userDto) throws GlobalExceptionHandler;
	 public String loginUser(LoginDto loginDto) throws GlobalExceptionHandler;
	 public String updateUserDetails(LoginDto loginDto, UserDto userDto) throws GlobalExceptionHandler;
}