package com.urs.booking.service;

import com.urs.booking.api.FlightFeign;
import com.urs.booking.api.PassengerFeign;
import com.urs.booking.api.UserFeign;
import com.urs.booking.dto.BookingDTO;
import com.urs.booking.dto.FlightDTO;
import com.urs.booking.dto.LoginDto;
import com.urs.booking.dto.PassengerDTO;
import com.urs.booking.entity.Booking;
import com.urs.booking.exception.GlobalExceptionHandler;
import com.urs.booking.repository.BookingRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Service("bookingService")
@Transactional
public class BookingServiceImpl implements BookingService {
	
	@Autowired
    BookingRepository bookingRepository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	DiscoveryClient client;
	
	@Autowired
	FlightFeign flightFeign;
	
	@Autowired
	PassengerFeign passengerFeign;
	
	@Autowired
	UserFeign userFeign;
	
	String BookingNotFoundOrUnauthorized = "Booking not found or unauthorized";
	String CANCELLED = "CANCELLED";
	String LoginSuccessful = "Login successful.";
	
	private static final Random random = new Random();
    
    @Override
    public BookingDTO createBooking(String userId, String password, String flightId, List<PassengerDTO> passengerDto) throws GlobalExceptionHandler {
        authenticateUser(userId, password);
        validateFlightAndSeats(flightId, passengerDto);
        FlightDTO flight = getFlight(flightId);
        String pnr = generatePNR();
        LocalDate bookingDate = LocalDate.now();
        LocalDate departureDate = flight.getAvailableDate();
        String departureTime = flight.getDepartureTime();
        double baseFare = flight.getFare();
        double totalFare = baseFare * passengerDto.size();

        Booking booking = new Booking();
        booking.setPnr(pnr);
        booking.setBookingDate(bookingDate);
        booking.setDepartureDate(departureDate);
        booking.setDepartureTime(departureTime);
        booking.setTotalFare(totalFare);
        booking.setFlightId(flightId);
        booking.setUserId(userId);
        booking.setNoOfSeats(passengerDto.size());
        booking.setStatus("BOOKED");

        addPassengersToBooking(pnr, passengerDto);
        updateFlightSeats(flightId, passengerDto.size());
        BookingDTO bookingDto = modelMapper.map(booking, BookingDTO.class);
        List<PassengerDTO> passengerList = fetchPassengersByBooking(pnr);
        bookingRepository.save(booking);
        bookingDto.setPassengerList(passengerList);
        return bookingDto;
    }

    private void authenticateUser(String userId, String password) throws GlobalExceptionHandler {
        if (userId == null || userId.isEmpty() || password == null || password.isEmpty()) {
            throw new GlobalExceptionHandler("Invalid user credentials");
        }
        LoginDto loginDto = new LoginDto(userId, password);
        ResponseEntity<String> res = userFeign.authenticateUser(loginDto);
        if (res.getStatusCode() != HttpStatus.OK || !LoginSuccessful.equals(res.getBody())) {
            throw new GlobalExceptionHandler("Invalid user credentials");
        }
    }

    private void validateFlightAndSeats(String flightId, List<PassengerDTO> passengerDto) throws GlobalExceptionHandler {
        if (passengerDto.size() > 4) {
            throw new GlobalExceptionHandler("Cannot book more than 4 seats");
        }
        if (flightId == null || flightId.isEmpty()) {
            throw new GlobalExceptionHandler("Invalid flight ID");
        }
    }

    private FlightDTO getFlight(String flightId) throws GlobalExceptionHandler {
        ResponseEntity<FlightDTO> response = flightFeign.getSpecificFlight(flightId);
        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new GlobalExceptionHandler("Flight not found");
        }
        FlightDTO flight = response.getBody();
        if (flight == null) {
            throw new GlobalExceptionHandler("Flight not found");
        }
        return flight;
    }

    private void addPassengersToBooking(String pnr, List<PassengerDTO> passengerDto) throws GlobalExceptionHandler {
        for (PassengerDTO pDto : passengerDto) {
            ResponseEntity<String> passengerResponse = passengerFeign.addPassengers(pnr, pDto);
            if (passengerResponse.getStatusCode() != HttpStatus.CREATED) {
                throw new GlobalExceptionHandler("Failed to add passenger");
            }
        }
    }

    private void updateFlightSeats(String flightId, int seats) throws GlobalExceptionHandler {
        ResponseEntity<String> updateResponse = flightFeign.updateSeats(flightId, seats);
        if (updateResponse.getStatusCode() != HttpStatus.OK) {
            throw new GlobalExceptionHandler("Failed to update available seats");
        }
    }

    private List<PassengerDTO> fetchPassengersByBooking(String pnr) throws GlobalExceptionHandler {
        ResponseEntity<List<PassengerDTO>> passengersResponse = passengerFeign.getPassengersByBooking(pnr);
        if (passengersResponse.getStatusCode() != HttpStatus.OK || passengersResponse.getBody() == null) {
            throw new GlobalExceptionHandler("Failed to fetch passengers");
        }
        return passengersResponse.getBody();
    }

    @Override
    public BookingDTO getBookingByPnr(String pnr, String userId, String password) throws GlobalExceptionHandler {
    	// Simulate user authentication (should call UserMS in real scenario)
    	if (userId == null || userId.isEmpty() || password == null || password.isEmpty()) {
			throw new GlobalExceptionHandler("Invalid user credentials");
		}
    	// Call Login API of UserMS to validate userId and password
    	LoginDto loginDto = new LoginDto(userId, password);
    	ResponseEntity<String> response = userFeign.authenticateUser(loginDto);
    	if (response.getStatusCode() != HttpStatus.OK || !LoginSuccessful.equals(response.getBody())) {
  			throw new GlobalExceptionHandler("Invalid user credentials");
    	}
        Booking booking = bookingRepository.findByPnrAndUserId(pnr, userId)
                .orElseThrow(() -> new GlobalExceptionHandler(BookingNotFoundOrUnauthorized));
        BookingDTO bookingDto = modelMapper.map(booking, BookingDTO.class);
        ResponseEntity<List<PassengerDTO>> passengersResponse = passengerFeign.getPassengersByBooking(pnr);
        if (passengersResponse.getStatusCode() != HttpStatus.OK || passengersResponse.getBody() == null) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch passengers");
		}
		List<PassengerDTO> passengerList = passengersResponse.getBody();
		bookingDto.setPassengerList(passengerList);
		return bookingDto;
    }

    @Override
    public void cancelBooking(String pnr, String userId, String password) throws GlobalExceptionHandler {
    	if (userId == null || userId.isEmpty() || password == null || password.isEmpty()) {
			throw new GlobalExceptionHandler("Invalid user credentials");
		}
    	// Call Login API of UserMS to validate userId and password
    	LoginDto loginDto = new LoginDto(userId, password);
    	ResponseEntity<String> response = userFeign.authenticateUser(loginDto);
    	if (response.getStatusCode() != HttpStatus.OK || !LoginSuccessful.equals(response.getBody())) {
  			throw new GlobalExceptionHandler("Invalid user credentials");
    	}
        Booking booking = bookingRepository.findByPnrAndUserId(pnr, userId)
                .orElseThrow(() -> new GlobalExceptionHandler(BookingNotFoundOrUnauthorized));
        if (CANCELLED.equalsIgnoreCase(booking.getStatus())) {
            throw new GlobalExceptionHandler("Booking is already cancelled");
        }
        booking.setStatus(CANCELLED);
        //Update available seats in FlightMS
        ResponseEntity<String> updateResponse = flightFeign.updateSeats(booking.getFlightId(), booking.getNoOfSeats() * -1);
        if (updateResponse.getStatusCode() != HttpStatus.OK) {
			throw new GlobalExceptionHandler("Failed to update available seats");
        }
        		
        bookingRepository.save(booking);
    }

	@Override
	public void updateBookingStatus(String pnr, String status, String userName, String password) throws GlobalExceptionHandler {
		if (userName == null || userName.isEmpty() || password == null || password.isEmpty()) {
			throw new GlobalExceptionHandler("Invalid user credentials");
		}
		// Call Login API of UserMS to validate userId and password
		LoginDto loginDto = new LoginDto(userName, password);
		ResponseEntity<String> response = userFeign.authenticateUser(loginDto);
		if (response.getStatusCode() != HttpStatus.OK || !LoginSuccessful.equals(response.getBody())) {
  			throw new GlobalExceptionHandler("Invalid user credentials");
		}
		Booking booking = bookingRepository.findByPnrAndUserId(pnr, userName)
				.orElseThrow(() -> new GlobalExceptionHandler("Booking not found or unauthorized"));
		if (!"CONFIRMED".equalsIgnoreCase(booking.getStatus()) && !CANCELLED.equalsIgnoreCase(booking.getStatus()) && !"BOOKED".equalsIgnoreCase(booking.getStatus())) {
			throw new GlobalExceptionHandler("Only CONFIRMED, CANCELLED or BOOKED bookings can be updated");
		}
		if (!"CONFIRMED".equalsIgnoreCase(status) && !CANCELLED.equalsIgnoreCase(status) && !"COMPLETED".equalsIgnoreCase(status)) {
			throw new GlobalExceptionHandler("Status must be either 'CONFIRMED', 'CANCELLED', or 'COMPLETED'");
		}
		booking.setStatus(status);
		bookingRepository.save(booking);
	}
	
	private static String generatePNR() {
		String alphabets = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String numbers = "0123456789";

		StringBuilder result = new StringBuilder(6);
		for (int i = 0; i < 3; i++) {
			result.append(alphabets.charAt(random.nextInt(alphabets.length())));
		}
		for (int i = 0; i < 3; i++) {
			result.append(numbers.charAt(random.nextInt(numbers.length())));
		}

		return result.toString();
	}
}