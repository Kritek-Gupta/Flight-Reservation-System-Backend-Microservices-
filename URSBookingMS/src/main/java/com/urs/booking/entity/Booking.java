package com.urs.booking.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    private String pnr;
    private LocalDate bookingDate;
    private LocalDate departureDate;
    private String departureTime;
    private Double totalFare;
    private String flightId;
    private String userId;
    private Integer noOfSeats;
    private String status; // e.g., AVAILABLE, BOOKED, SELECTED, CANCELLED
}
