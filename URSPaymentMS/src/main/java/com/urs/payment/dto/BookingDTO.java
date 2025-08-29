package com.urs.payment.dto;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
	
    private String pnr;
    
    @DateTimeFormat(pattern = "dd-mm-yyyy")
    @NotBlank(message = "{BookingDTO.bookingDate.NotBlank}")
    private LocalDate bookingDate;
    
    @DateTimeFormat(pattern = "dd-mm-yyyy")
    @NotBlank(message = "{BookingDTO.departureDate.NotBlank}")
    private LocalDate departureDate;
    
    @NotBlank(message = "{BookingDTO.departureTime.NotBlank}")
    private String departureTime;
    
    @NotNull(message = "{BookingDTO.totalFare.NotNull}")
    private Double totalFare;
    
    @NotBlank(message = "{BookingDTO.flightId.NotBlank}")
    private String flightId;
    
    @NotBlank(message = "{BookingDTO.userId.NotBlank}")
    private String userId;
    
    @NotNull(message = "{BookingDTO.noOfSeats.NotNull}")
    @Min(value = 1, message = "{BookingDTO.noOfSeats.Min}")
    @Max(value = 4, message = "{BookingDTO.noOfSeats.Max}")
    private Integer noOfSeats;
    
    @NotBlank(message = "{BookingDTO.status.NotBlank}")
    private String status;
    
    private List<PassengerDTO> passengerList;
}