package com.urs.passenger.service;

import com.urs.passenger.dto.PassengerDTO;
import com.urs.passenger.entity.Passenger;
import com.urs.passenger.exception.GlobalExceptionHandler;
import com.urs.passenger.repository.PassengerRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("passengerService")
@Transactional
public class PassengerServiceImpl implements PassengerService {
	@Autowired
    PassengerRepository passengerRepository;
	
	@Autowired
	ModelMapper modelMapper;
	
    private static final int MAX_PASSENGERS_PER_BOOKING = 4;

    @Override
    public PassengerDTO addPassenger(PassengerDTO passengetDto) throws GlobalExceptionHandler {
        long count = passengerRepository.countByBookingPnr(passengetDto.getBookingPnr());
        if (count >= MAX_PASSENGERS_PER_BOOKING) {
            throw new GlobalExceptionHandler("Cannot add more than 4 passengers per booking");
        }
        Passenger passenger = modelMapper.map(passengetDto, Passenger.class);
        passengerRepository.save(passenger);
        return passengetDto;
    }

    @Override
    public List<PassengerDTO> getPassengersByBookingPnr(String bookingPnr) throws GlobalExceptionHandler {
        List<PassengerDTO> passengers = passengerRepository.findByBookingPnr(bookingPnr)
                .stream()
                .map(p -> modelMapper.map(p, PassengerDTO.class))
                .toList();
        if(passengers.isEmpty()) {
			throw new GlobalExceptionHandler("No passengers found for the given PNR");
		}
        return passengers;
    }

    @Override
    public PassengerDTO getPassengerById(Long id) throws GlobalExceptionHandler {
        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler("Passenger not found"));
        return modelMapper.map(passenger, PassengerDTO.class);
    }

    @Override
    public PassengerDTO updatePassenger(Long id, PassengerDTO dto) throws GlobalExceptionHandler {
        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler("Passenger not found"));
        BeanUtils.copyProperties(dto, passenger, "passengerId");
        passenger = passengerRepository.save(passenger);
        return modelMapper.map(passenger, PassengerDTO.class);
    }

    @Override
    public void deletePassenger(Long id) throws GlobalExceptionHandler {
        if (!passengerRepository.existsById(id)) {
            throw new GlobalExceptionHandler("Passenger not found");
        }
        passengerRepository.deleteById(id);
    }

	@Override
	public List<PassengerDTO> addPassengers(List<PassengerDTO> dtos) throws GlobalExceptionHandler {
		if(dtos == null || dtos.isEmpty()) {
			throw new GlobalExceptionHandler("Passenger list cannot be empty");
		}
		List<Passenger> passengers = dtos.stream()
				.map(dto -> modelMapper.map(dto, Passenger.class))
				.toList();
		
		passengerRepository.saveAll(passengers);
		
		return passengers.stream()
				.map(p -> modelMapper.map(p, PassengerDTO.class))
				.toList();
	}
}
