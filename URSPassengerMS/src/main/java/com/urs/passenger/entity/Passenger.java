package com.urs.passenger.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long passengerId;

    @Column(nullable = false)
    private String passengerName;
    private Integer passengerAge;
    private String gender;
    private String bookingPnr;
    private String mealPreference;
    private String seatPreference;
}
