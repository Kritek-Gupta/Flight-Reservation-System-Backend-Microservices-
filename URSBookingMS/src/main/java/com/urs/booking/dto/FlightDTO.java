/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urs.booking.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightDTO {
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
