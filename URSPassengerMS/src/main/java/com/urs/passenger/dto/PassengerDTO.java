package com.urs.passenger.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PassengerDTO {
    @NotBlank(message = "{PassengerDTO.passengerName.NotBlank}")
    @Pattern(regexp = "^[A-Za-z0-9- ]+$", message = "{PassengerDTO.passengerName.Pattern}")
    @Size(max = 50, message = "{PassengerDTO.passengerName.Size}")
    private String passengerName;

    @NotNull(message = "{PassengerDTO.passengerAge.NotNull}")
    @Min(value = 0, message = "{PassengerDTO.passengerAge.Min}")
    @Max(value = 80, message = "{PassengerDTO.passengerAge.Max}")
    private Integer passengerAge;

    @NotBlank(message = "{PassengerDTO.gender.NotBlank}")
    @Pattern(regexp = "^(Male|Female|Other)$", message = "{PassengerDTO.gender.Pattern}")
    private String gender;

    @Pattern(regexp = "^[A-Z]{3}\\d{3}$", message = "{PassengerDTO.bookingPnr.Pattern}")
    private String bookingPnr;
    
    private String mealPreference;
    private String seatPreference;
}