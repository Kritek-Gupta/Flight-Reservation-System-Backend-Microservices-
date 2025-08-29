package com.urs.passenger.service;

import java.util.List;

import com.urs.passenger.dto.PassengerDTO;
import com.urs.passenger.exception.GlobalExceptionHandler;

public interface PassengerService {
    PassengerDTO addPassenger(PassengerDTO dto) throws GlobalExceptionHandler;
    List<PassengerDTO> getPassengersByBookingPnr(String bookingPnr) throws GlobalExceptionHandler;
    PassengerDTO getPassengerById(Long id) throws GlobalExceptionHandler;
    PassengerDTO updatePassenger(Long id, PassengerDTO dto) throws GlobalExceptionHandler;
    void deletePassenger(Long id) throws GlobalExceptionHandler;
    
    List<PassengerDTO> addPassengers(List<PassengerDTO> dtos) throws GlobalExceptionHandler;
}
