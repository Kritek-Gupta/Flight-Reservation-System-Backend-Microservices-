package com.urs.payment.service;

import com.urs.payment.entity.Payment;
import com.urs.payment.exception.GlobalExceptionHandler;
import com.urs.payment.repository.PaymentRepository;
import com.urs.payment.api.BookingFeign;
import com.urs.payment.dto.BookingDTO;
import com.urs.payment.dto.PaymentDTO;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Service("paymentService")
public class PaymentServiceImpl implements PaymentService {
	
	@Autowired
	private PaymentRepository paymentRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	BookingFeign bookingFeign;
	
	@Override
    public String processPayment(PaymentDTO paymentDTO, String bookingPnr, String userId, String password) throws GlobalExceptionHandler {
        // Fetch booking details from Booking MS using bookingPnr, userId, and password
		ResponseEntity<BookingDTO> response = bookingFeign.getBookingDetails(bookingPnr, userId, password);
    	if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
			throw new GlobalExceptionHandler("Booking not found or invalid credentials");
		}
    	BookingDTO booking = response.getBody();
    	if(booking == null) {
    		throw new GlobalExceptionHandler("Booking not found");
		}
    	
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
        YearMonth expiry = YearMonth.parse(paymentDTO.getExpiryDate(), formatter);
        YearMonth now = YearMonth.now();
        if (expiry.isBefore(now)) {
            throw new GlobalExceptionHandler("Card is expired. Please use a valid card.");
        }
        
//        if pnr is already confirmed, throw exception
       if (booking.getStatus().equalsIgnoreCase("CONFIRMED")) {
    	   throw new GlobalExceptionHandler("Booking is already CONFIRMED for PNR: " + bookingPnr);
       }
    	
		// Calculate total fare with discount
    	
    	double totalFare = booking.getTotalFare();
        // Apply discount
        double discount = 0.0;
        if ("credit".equalsIgnoreCase(paymentDTO.getCardType())) {
            discount = (totalFare * 10) / 100.00;
        } else if ("debit".equalsIgnoreCase(paymentDTO.getCardType())) {
            discount = (totalFare * 5) / 100.00;
        }
        double finalAmount = totalFare - discount;
        Payment payment = modelMapper.map(paymentDTO, Payment.class);
        payment.setTotalFare(finalAmount);
        
        // Here, you would typically call the Booking MS to update the booking status to "CONFIRMED"
        ResponseEntity<String> statusResponse = bookingFeign.updateBookingStatus(bookingPnr, "CONFIRMED", userId, password);
        if (!statusResponse.getStatusCode().is2xxSuccessful()) {
        	throw new GlobalExceptionHandler("Failed to update booking status");
        }
        paymentRepository.save(payment);
        return "PAYMENT SUCCESSFUL. Total amount after discount: " + finalAmount + ". Booking status updated to CONFIRMED For PNR: " + bookingPnr;
    }
}