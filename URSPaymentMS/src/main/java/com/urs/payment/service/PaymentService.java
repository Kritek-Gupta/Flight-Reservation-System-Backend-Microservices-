package com.urs.payment.service;

import com.urs.payment.dto.PaymentDTO;
import com.urs.payment.exception.GlobalExceptionHandler;

public interface PaymentService {
	public String processPayment(PaymentDTO paymentDTO, String bookingPnr, String userId, String password) throws GlobalExceptionHandler;
}
