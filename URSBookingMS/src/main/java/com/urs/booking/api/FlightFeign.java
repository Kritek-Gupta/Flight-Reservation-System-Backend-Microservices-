package com.urs.booking.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import com.urs.booking.dto.FlightDTO;

@FeignClient(name = "URSFlightMS")
public interface FlightFeign {
	
	@GetMapping("api/flights/{flightId}")
	ResponseEntity<FlightDTO> getSpecificFlight(@PathVariable String flightId);
	
	@PutMapping("api/flights/updateSeats/{flightId}/{seats}")
	ResponseEntity<String> updateSeats(@PathVariable String flightId, @PathVariable int seats);
}
