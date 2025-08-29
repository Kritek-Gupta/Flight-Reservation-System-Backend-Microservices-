package com.urs.user.dto;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @NotEmpty(message = "{Username.must.not.be.empty}")
    @Pattern(regexp = "^[a-z0-9_.!@#$%^&*-]+$", message = "{Username.can.only.contain.lowercase.letters.numbers.underscores.dots.and.hyphens}")
    private String username;

    @NotEmpty(message = "{Password.must.not.be.empty}")
    @Size(min = 8, message = "{Password.must.be.at.least.8.characters.long}")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
        message = "{Password.must.contain.at.least.one.uppercase.letter.one.lowercase.letter.one.digit.and.one.special.character}"
    )
    private String password;

    @NotEmpty(message = "{Name.must.not.be.empty}")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "{Name.must.contain.only.letters.and.spaces}")
    private String name;

    @NotEmpty(message = "{Email.must.not.be.empty}")
    @Email(message = "{Email.should.be.valid}")
    private String email;

    @NotEmpty(message = "{City.must.not.be.empty}")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "{City.must.contain.only.letters.and.spaces}")
    private String city;

    @NotEmpty(message = "{Contact.number.must.not.be.empty}")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "{Contact.number.must.be.a.valid.10.digit.Indian.mobile.number.starting.with.6.9}")
    private String contactNumber;

   public void setName(String name) {
    	  if (name == null || name.trim().isEmpty()) {
              this.name = name;
              return;
          }
          this.name = Stream.of(name.trim().split("\\s+"))
                  .filter(word -> !word.isEmpty())
                  .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase())
                  .collect(Collectors.joining(" "));
    }
}