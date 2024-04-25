package com.authentication.jas.authentications.providers;

import com.authentication.jas.authentications.OtpAuthentication;
import com.authentication.jas.database.entities.User;
import com.authentication.jas.database.repository.UserRepository;
import com.authentication.jas.services.OtpService;
import com.authentication.jas.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Provides the logic for OTP (One-Time Password) based authentication.
 * This class implements {@link AuthenticationProvider} to provide a custom authentication method
 * that uses OTPs for verifying user identity.
 */
@Component
@RequiredArgsConstructor
public class OtpAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final OtpService otpService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    /**
     * Authenticates a user based on OTP credentials.
     * This method retrieves user details from the username provided in the authentication object,
     * checks if the OTP is valid and matches the provided credentials, and then returns a fully authenticated
     * {@link OtpAuthentication} object if successful.
     *
     * @param authentication the authentication request object containing the principal and credentials
     * @return a fully authenticated object including authorities
     * @throws AuthenticationException if authentication fails due to invalid credentials or other authentication issues
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = String.valueOf(authentication.getCredentials());

        UserDetails userDetails = userService.loadUserByUsername(username);
        Optional<User> dbUser = userRepository.findUserByEmail(username);

        if (dbUser.isPresent()) {
            User user = (User) userDetails;

            if (otpService.checkOtpIsValid(user.getOtp().getExpiresAt())) {
                if (passwordEncoder.matches(password, user.getOtp().getOtpCode())) {
                    return new OtpAuthentication(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                }
            }

            throw new BadCredentialsException("Bad credentials!");
        } else {
            throw new UsernameNotFoundException("Username not found: " + username);
        }
    }

    /**
     * Determines if this AuthenticationProvider supports a given Authentication class.
     * This provider only supports {@link OtpAuthentication} class.
     *
     * @param authentication the type of authentication
     * @return true if the authentication type is supported by this provider, false otherwise
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(OtpAuthentication.class);
    }
}
