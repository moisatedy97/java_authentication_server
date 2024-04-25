package com.authentication.jas.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data Transfer Object for checking the validity of an OTP.
 * This class encapsulates the necessary data to verify an OTP, including the user's email and the OTP itself.
 */
@Data
@AllArgsConstructor
public class OtpCheckDto {

    /**
     * The email address associated with the OTP.
     * Must be a valid email format and cannot be null.
     */
    @Email
    @NotNull
    private String email;

    /**
     * The One-Time Password to be verified.
     * Must be exactly 4 characters in length.
     */
    @NotNull
    @Size(min=4, max=4)
    private String otp;
}
