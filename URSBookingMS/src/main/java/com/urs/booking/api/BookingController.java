package com.urs.booking.api;

import com.urs.booking.dto.BookingDTO;
import com.urs.booking.dto.PassengerDTO;
import com.urs.booking.exception.GlobalExceptionHandler;
import com.urs.booking.service.BookingService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@Validated
public class BookingController {

	@Autowired
	BookingService bookingService;
	

	@PostMapping("/{flightId}/{userName}/{password}")
	public ResponseEntity<BookingDTO> createBooking(
			@PathVariable @NotBlank(message = "Flight ID is required") String flightId,
			@PathVariable @NotBlank(message = "User ID is required") @Pattern(regexp = "^[a-z0-9_.-]+$", message = "{Username.can.only.contain.lowercase.letters.numbers.underscores.dots.and.hyphens}") String userName,
			@PathVariable @NotBlank(message = "Password is required") String password,
			@Valid @RequestBody List<@Valid PassengerDTO> passengerDto) throws GlobalExceptionHandler {
		BookingDTO response = bookingService.createBooking(userName, password, flightId, passengerDto);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping("/{pnr}/{userName}/{password}")
	public ResponseEntity<BookingDTO> getBookingByPnr(
			@PathVariable @Pattern(regexp = "^[A-Z]{3}\\d{3}$", message = "PNR must start with 2 uppercase letters followed by numbers, e.g., UG1234") String pnr,
			@PathVariable @NotBlank(message = "User ID is required") @Pattern(regexp = "^[a-z0-9_.-]+$", message = "{Username.can.only.contain.lowercase.letters.numbers.underscores.dots.and.hyphens}") String userName,
			@PathVariable @NotBlank(message = "Password is required") String password) throws GlobalExceptionHandler {
		return ResponseEntity.ok(bookingService.getBookingByPnr(pnr, userName, password));
	}

	@PutMapping("/cancel/{pnr}/{userName}/{password}")
	@CircuitBreaker(name = "bookingServiceCircuitBreaker", fallbackMethod = "cancelBookingFallback")
	public ResponseEntity<String> cancelBooking(
			@PathVariable @Pattern(regexp = "^[A-Z]{3}\\d{3}$", message = "PNR must start with 2 uppercase letters followed by numbers, e.g., UG1234") String pnr,
			@PathVariable @NotBlank(message = "User ID is required") @Pattern(regexp = "^[a-z0-9_.-]+$", message = "{Username.can.only.contain.lowercase.letters.numbers.underscores.dots.and.hyphens}") String userName,
			@PathVariable @NotBlank(message = "Password is required") String password) throws GlobalExceptionHandler {
		bookingService.cancelBooking(pnr, userName, password);
		return ResponseEntity.ok("Booking cancelled successfully");
	}
	
	public ResponseEntity<String> cancelBookingFallback(String pnr, String userName, String password, Throwable throwable) {
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body("Cancel Booking service is currently unavailable. Please try again later." + throwable.getMessage());
	}

	// add endpoint to edit status of booking
	@PutMapping("/status/{pnr}/{status}/{userName}/{password}")
	public ResponseEntity<String> updateBookingStatus(
			@PathVariable @Pattern(regexp = "^[A-Z]{3}\\d{3}$", message = "PNR must start with 2 uppercase letters followed by numbers, e.g., UG1234") String pnr,
			@PathVariable @Pattern(regexp = "^(CONFIRMED|CANCELLED|COMPLETED|BOOKED)$", message = "Status must be either 'CONFIRMED', 'CANCELLED', or 'COMPLETED'") String status,
			@PathVariable @NotBlank(message = "User ID is required") @Pattern(regexp = "^[a-z0-9_.-]+$", message = "{Username.can.only.contain.lowercase.letters.numbers.underscores.dots.and.hyphens}") String userName,
			@PathVariable @NotBlank(message = "Password is required") String password) throws GlobalExceptionHandler {
		bookingService.updateBookingStatus(pnr, status, userName, password);
		return ResponseEntity.ok("Booking status updated successfully");
	}
}