package com.urs.booking.api;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.urs.booking.dto.PassengerDTO;

@FeignClient(name = "URSPassengerMS")
public interface PassengerFeign {	
	@PostMapping("/api/passengers/{pnr}")
	ResponseEntity<String> addPassengers(@PathVariable String pnr, @RequestBody PassengerDTO passengerDto);
	
	@GetMapping("/api/passengers/booking/{pnr}")
	ResponseEntity<List<PassengerDTO>> getPassengersByBooking(@PathVariable String pnr);
}
