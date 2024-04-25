package com.authentication.jas.authentications.providers;

import com.authentication.jas.authentications.UsernamePasswordAuthentication;
import com.authentication.jas.database.entities.User;
import com.authentication.jas.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Provides authentication logic for username and password based authentication.
 * This class implements {@link AuthenticationProvider} to provide a custom authentication method
 * that uses username and password for verifying user identity.
 */
@Component
@RequiredArgsConstructor
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Authenticates a user based on username and password credentials.
     * This method retrieves user details from the username provided in the authentication object,
     * checks if the password is valid and matches the stored password, and then returns a fully authenticated
     * {@link UsernamePasswordAuthentication} object if successful.
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

        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            User user = (User) userDetails;

            userService.renewUserOtp(user);

            return new UsernamePasswordAuthentication(userDetails.getUsername(), userDetails.getPassword());
        } else {
            throw new BadCredentialsException("Bad credentials!");
        }
    }

    /**
     * Determines if the provided authentication type is supported by this provider.
     * This implementation specifically supports {@link UsernamePasswordAuthentication}.
     *
     * @param authentication the class of the authentication request
     * @return true if the authentication type is supported, false otherwise
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthentication.class);
    }
}
