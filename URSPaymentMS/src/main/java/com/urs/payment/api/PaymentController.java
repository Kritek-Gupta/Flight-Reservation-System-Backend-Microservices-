package com.urs.payment.api;

import com.urs.payment.dto.PaymentDTO;
import com.urs.payment.exception.GlobalExceptionHandler;
import com.urs.payment.service.PaymentService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@Validated
public class PaymentController {
	
    @Autowired
    private PaymentService paymentService;
    
    @PostMapping("/make/{bookingPnr}/{userId}/{password}")
    public ResponseEntity<String> makePayment(
            @Valid @RequestBody PaymentDTO payment,
            @PathVariable @Pattern(regexp = "^[A-Z]{3}\\d{3}$", message = "Invalid booking PNR") String bookingPnr,
            @PathVariable @NotBlank(message = "User ID is required") String userId,
            @PathVariable @NotBlank(message = "Password is required") String password) throws GlobalExceptionHandler {
        String response = paymentService.processPayment(payment, bookingPnr, userId, password);
        return ResponseEntity.ok(response);
    }
}