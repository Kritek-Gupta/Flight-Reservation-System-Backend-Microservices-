package com.urs.flight.service;

import java.time.LocalDate;
import java.util.List;

import com.urs.flight.dto.FlightDTO;
import com.urs.flight.exception.GlobalExceptionHandler;

public interface FlightService {

	List<FlightDTO> getFlights(String source, String destination, LocalDate journeyDate) throws GlobalExceptionHandler;

	FlightDTO getFlightById(String flightId) throws GlobalExceptionHandler;

	void updateAvailableSeats(String flightId, int seatsToBook) throws GlobalExceptionHandler;

}
