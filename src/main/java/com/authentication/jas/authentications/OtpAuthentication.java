package com.authentication.jas.authentications;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Represents an authentication mechanism specifically for one-time password (OTP) based authentication.
 * Unlike traditional username and password authentication, this class uses email and a code.
 */
public class OtpAuthentication extends UsernamePasswordAuthenticationToken {

    /**
     * Constructs an OtpAuthentication object without any granted authorities.
     * This constructor is typically used before the user is authenticated.
     *
     * @param principal   the identity of the principal being authenticated
     * @param credentials the credentials that prove the principal is correct, typically a password or OTP code
     */
    public OtpAuthentication(Object principal, Object credentials) {
        super(principal, credentials);
    }

    /**
     * Constructs an OtpAuthentication object with specified user authorities.
     * This constructor is used when the user has been authenticated and their authorities are known.
     *
     * @param principal   the identity of the principal being authenticated
     * @param credentials the credentials that prove the principal is correct, typically a password or OTP code
     * @param authorities the collection of granted authorities for the principal
     */
    public OtpAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
