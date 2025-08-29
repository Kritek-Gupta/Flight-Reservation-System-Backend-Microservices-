package com.urs.flight.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.urs.flight.dto.FlightDTO;
import com.urs.flight.entity.Flight;
import com.urs.flight.exception.GlobalExceptionHandler;
import com.urs.flight.repository.FlightsRepository;;

@Service("flightService")
public class FlightServiceImpl implements FlightService {

	@Autowired
	private FlightsRepository flightsRepository;
	
	@Autowired
	ModelMapper modelMapper;

	@Override
	public List<FlightDTO> getFlights(String source, String destination, LocalDate journeyDate) throws GlobalExceptionHandler {

		List<Flight> flights = flightsRepository.findBySourceAndDestinationAndAvailableDate(source, destination, journeyDate);
		
		if (flights == null || flights.isEmpty()) {
			throw new GlobalExceptionHandler("No flights available for the given source, destination, and date.");
		}

		List<FlightDTO> availableFlights = new ArrayList<>();
		for (Flight f : flights) {
			FlightDTO flight = modelMapper.map(f, FlightDTO.class);
			availableFlights.add(flight);
		}

		return availableFlights;

	}

	@Override
	public FlightDTO getFlightById(String flightId) throws GlobalExceptionHandler {
		Flight flight = flightsRepository.findById(flightId)
				.orElseThrow(() -> new GlobalExceptionHandler("Flight not found with ID: " + flightId));
		return modelMapper.map(flight, FlightDTO.class);
	}
	
	//Update available seats after booking or cancellation
	@Override
	public void updateAvailableSeats(String flightId, int seatsToBook) throws GlobalExceptionHandler {
		Flight flight = flightsRepository.findById(flightId)
				.orElseThrow(() -> new GlobalExceptionHandler("Flight not found with ID: " + flightId));
		int updatedSeats = flight.getAvailableSeats() - seatsToBook;
		if (updatedSeats < 0) {
			throw new GlobalExceptionHandler("Not enough available seats.");
		}
		flight.setAvailableSeats(updatedSeats);
		flightsRepository.save(flight);
	}
}