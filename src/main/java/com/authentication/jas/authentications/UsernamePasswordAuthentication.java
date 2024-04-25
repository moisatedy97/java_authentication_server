package com.authentication.jas.authentications;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Extends {@link UsernamePasswordAuthenticationToken} to provide a custom authentication object
 * that primarily uses email and password for authentication purposes.
 */
public class UsernamePasswordAuthentication extends UsernamePasswordAuthenticationToken {

    /**
     * Constructs a new {@code UsernamePasswordAuthentication} object with the specified principal and credentials.
     * This constructor is typically used prior to authentication and does not include authorities, 
     * meaning the created authentication token is not yet authenticated.
     *
     * @param principal   the identity of the principal being authenticated
     * @param credentials the credentials that prove the principal is correct, typically a password
     */
    public UsernamePasswordAuthentication(Object principal, Object credentials) {
        super(principal, credentials);
    }

    /**
     * Constructs a new {@code UsernamePasswordAuthentication} object with the specified principal, credentials,
     * and authorities. This constructor should be used when the principal has been authenticated and the authorities
     * are known.
     *
     * @param principal   the authenticated principal or the subject of the token
     * @param credentials the credentials confirming the principal's identity
     * @param authorities the authorities granted to the principal, typically roles or permissions
     */
    public UsernamePasswordAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
