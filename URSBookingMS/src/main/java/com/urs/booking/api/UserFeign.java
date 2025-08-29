package com.urs.booking.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.urs.booking.dto.LoginDto;

@FeignClient(name = "URSUserMS")
public interface UserFeign {
	@PostMapping("/api/users/login")
	ResponseEntity<String> authenticateUser(@RequestBody LoginDto loginDto);
}
