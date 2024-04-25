package com.authentication.jas.services;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Provides services for generating and validating one-time passwords (OTPs).
 */
@Service
@Transactional
@RequiredArgsConstructor
public class OtpService {

    /**
     * Generates a one-time password (OTP) as a four-digit code.
     * This method uses a strong instance of {@link SecureRandom} to ensure
     * the randomness and uniqueness of the OTP.
     *
     * @return A string representing the generated four-digit OTP.
     * @throws RuntimeException if no strong {@link SecureRandom} instance is available.
     */
    public String generateOtp() {
        String code;

        try {
            SecureRandom random = SecureRandom.getInstanceStrong();
            code = String.valueOf(random.nextInt(9000) + 1000);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Problem when generating the OTP", e);
        }

        return code;
    }

    /**
     * Validates if the provided OTP expiration date is still valid.
     * The OTP is considered valid if the expiration date (in milliseconds since epoch)
     * is greater than the current system time.
     *
     * @param expDate The expiration date of the OTP in milliseconds since epoch.
     * @return {@code true} if the current system time is less than the expiration date, {@code false} otherwise.
     */
    public boolean checkOtpIsValid(Long expDate) {
        return expDate > System.currentTimeMillis();
    }
}
