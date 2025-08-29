package com.urs.booking.service;

import java.util.List;

import com.urs.booking.dto.BookingDTO;
import com.urs.booking.dto.PassengerDTO;
import com.urs.booking.exception.GlobalExceptionHandler;

public interface BookingService {
    BookingDTO createBooking(String userId, String password, String flightId, List<PassengerDTO> passengerDto) throws GlobalExceptionHandler;
    BookingDTO getBookingByPnr(String pnr, String userId, String password) throws GlobalExceptionHandler;
    void cancelBooking(String pnr, String userId, String password) throws GlobalExceptionHandler;
	void updateBookingStatus(String pnr, String status, String userName, String password) throws GlobalExceptionHandler;
}
