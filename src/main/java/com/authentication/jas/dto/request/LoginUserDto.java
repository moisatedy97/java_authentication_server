package com.authentication.jas.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data Transfer Object for user login.
 * This class encapsulates the necessary data for a user attempting to log in,
 * including email, password, and a one-time password (OTP).
 */
@Data
@AllArgsConstructor
public class LoginUserDto {

    /**
     * The email address of the user attempting to log in.
     * Must be a valid email format and cannot be null.
     */
    @Email
    @NotNull
    private String email;

    /**
     * The password of the user.
     * Must be between 6 and 30 characters in length.
     */
    @Size(min=6, max=30)
    private String password;

    /**
     * The one-time password (OTP) for additional security during login.
     * Must be exactly 4 characters in length.
     */
    @Size(min = 4, max = 4)
    private String otp;
}
