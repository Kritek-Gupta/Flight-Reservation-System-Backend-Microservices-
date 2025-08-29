package com.urs.booking.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    @NotEmpty(message = "{Username.must.not.be.empty}")
    @Pattern(regexp = "^[a-z0-9_.-]+$", message = "{Username.can.only.contain.lowercase.letters.numbers.underscores.dots.and.hyphens}")
    private String username;

    @NotEmpty(message = "{Password.must.not.be.empty}")
    @Size(min = 8, message = "{Password.must.be.at.least.8.characters.long}")
    private String password;

}