package com.urs.payment.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import com.urs.payment.dto.BookingDTO;

@FeignClient(name = "URSBookingMS")
public interface BookingFeign {

	@GetMapping("/api/bookings/{bookingPnr}/{userId}/{password}")
	public ResponseEntity<BookingDTO> getBookingDetails(@PathVariable String bookingPnr, @PathVariable String userId,
			@PathVariable String password);

	@PutMapping("/api/bookings/status/{bookingPnr}/{status}/{userId}/{password}")
	public ResponseEntity<String> updateBookingStatus(@PathVariable String bookingPnr, @PathVariable String status,
			@PathVariable String userId, @PathVariable String password);
}
