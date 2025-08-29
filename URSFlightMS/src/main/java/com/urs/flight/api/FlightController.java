package com.urs.flight.api;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.urs.flight.dto.FlightDTO;
import com.urs.flight.exception.GlobalExceptionHandler;
import com.urs.flight.service.FlightService;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@RestController
@RequestMapping("/api/flights")
@Validated
public class FlightController {
	@Autowired
	private FlightService flightService;

	@GetMapping("/{source}/{destination}/{journeyDate}")
	public ResponseEntity<List<FlightDTO>> getFlightDetails(
			@PathVariable @Size(min = 2, max = 20, message = "{Size.FlightController.source}") @Pattern(regexp = "^[A-Za-z]+$", message = "{Pattern.FlightController.source}") String source,
			@PathVariable @Size(min = 2, max = 20, message = "{Size.FlightController.destination}") @Pattern(regexp = "^[A-Za-z]+$", message = "{Pattern.FlightController.destination}") String destination,
			@DateTimeFormat(pattern = "yyyy-MM-dd") @PathVariable LocalDate journeyDate) throws GlobalExceptionHandler {
		List<FlightDTO> availableFlights = flightService.getFlights(source, destination, journeyDate);
		return new ResponseEntity<>(availableFlights, HttpStatus.OK);
	}

	@GetMapping("/{flightId}")
	public ResponseEntity<FlightDTO> getFlightById(@PathVariable String flightId) throws GlobalExceptionHandler {
		FlightDTO flight = flightService.getFlightById(flightId);
		return new ResponseEntity<>(flight, HttpStatus.OK);
	}

	@PutMapping("/updateSeats/{flightId}/{seatsToBook}")
	public ResponseEntity<String> updateAvailableSeats(@PathVariable String flightId, @PathVariable int seatsToBook)
			throws GlobalExceptionHandler {
		flightService.updateAvailableSeats(flightId, seatsToBook);
		return new ResponseEntity<>("Available seats updated successfully", HttpStatus.OK);
	}
}