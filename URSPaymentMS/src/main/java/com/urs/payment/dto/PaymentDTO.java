package com.urs.payment.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    @NotBlank(message = "{PaymentDTO.cardType.NotBlank}")
    @Pattern(regexp = "(?i)credit|debit", message = "{PaymentDTO.cardType.Pattern}")
    private String cardType;

    @NotBlank(message = "{PaymentDTO.cardNumber.NotBlank}")
    @Pattern(regexp = "\\d{16}", message = "{PaymentDTO.cardNumber.Pattern}")
    private String cardNumber;

    @NotBlank(message = "{PaymentDTO.cardHolderName.NotBlank}")
    private String cardHolderName;

    @NotBlank(message = "{PaymentDTO.cvv.NotBlank}")
    @Pattern(regexp = "\\d{3}", message = "{PaymentDTO.cvv.Pattern}")
    private String cvv;

    @NotBlank(message = "{PaymentDTO.expiryDate.NotBlank}")
    @Pattern(regexp = "^(0[1-9]|1[0-2])/\\d{2}$", message = "{PaymentDTO.expiryDate.Pattern}")
    private String expiryDate;
}