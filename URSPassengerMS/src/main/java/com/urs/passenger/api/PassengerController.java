package com.urs.passenger.api;

import com.urs.passenger.dto.PassengerDTO;
import com.urs.passenger.exception.GlobalExceptionHandler;
import com.urs.passenger.service.PassengerService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



import java.util.List;

@RestController
@RequestMapping("/api/passengers")
@Validated
public class PassengerController {

	@Autowired
	PassengerService passengerService;
	

	@PostMapping("/{bookingPnr}")
	public ResponseEntity<PassengerDTO> addPassenger(
			@Pattern(regexp = "^[A-Z]{3}\\d{3}$", message = "{PassengerDTO.bookingPnr.Pattern}") @PathVariable String bookingPnr,
			@Valid @RequestBody PassengerDTO dto) throws GlobalExceptionHandler {
		dto.setBookingPnr(bookingPnr);
		PassengerDTO response = passengerService.addPassenger(dto);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PostMapping("/addPassengers")
	public ResponseEntity<List<PassengerDTO>> addPassengers(@Valid @RequestBody List<PassengerDTO> dtos)
			throws GlobalExceptionHandler {
		List<PassengerDTO> response = passengerService.addPassengers(dtos);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping("/booking/{pnr}")
	public ResponseEntity<List<PassengerDTO>> getPassengersByBookingPnr(
			@Pattern(regexp = "^[A-Z]{3}\\d{3}$", message = "{PassengerDTO.bookingPnr.Pattern}") @PathVariable String pnr) throws GlobalExceptionHandler {
		return ResponseEntity.ok(passengerService.getPassengersByBookingPnr(pnr));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletePassenger(@PathVariable Long id) throws GlobalExceptionHandler {
		passengerService.deletePassenger(id);
		return new ResponseEntity<>("Passenger deleted successfully", HttpStatus.OK);
	}
}
