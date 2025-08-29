package com.urs.flight.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Flight {
	@Id
	private String flightId;
	private String airLine;
	private String source;
	private String destination;
	private String arrivalTime;
	private String departureTime;
	private int availableSeats;
	private LocalDate availableDate;
	private double fare;
}
