package com.authentication.jas.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Class that contains all jwt token actions
 */
@Service
public class JwtService {

    @Value("${application.security.jwt.longevity}")
    private long jwtTokenLongevity;
    @Value("${application.security.jwt.refresh-token.longevity}")
    private long jwtRefreshTokenLongevity;

    /**
     * Generates a JWT token without extra claims.
     *
     * @param userDetails the user details to include in the token
     * @return the generated JWT token
     */
    public String generateJwtToken(UserDetails userDetails) {
        return generateJwtToken(new HashMap<>(), userDetails);
    }

    /**
     * Generates a JWT token with extra claims.
     *
     * @param extraClaims additional claims to include in the token
     * @param userDetails the user details to include in the token
     * @return the generated JWT token
     */
    public String generateJwtToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtTokenLongevity * 1000);
    }

    /**
     * Generates a JWT refresh token.
     *
     * @param userDetails the user details to include in the refresh token
     * @return the generated JWT refresh token
     */
    public String generateJwtRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, jwtRefreshTokenLongevity * 1000);
    }

    /**
     * Builds a JWT token with specified claims, user details, and expiration.
     *
     * @param extraClaims  additional claims to include in the token
     * @param userDetails  the user details to include in the token
     * @param expiration   the expiration time of the token in milliseconds
     * @return the generated JWT token
     */
    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validates a JWT token by comparing the subject with user details and checking if the token is expired.
     *
     * @param token       the JWT token to validate
     * @param userDetails the user details to compare with the token subject
     * @return true if the token is valid and not expired, false otherwise
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userEmail = extractJwtSubject(token);

        return (userEmail.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Checks if a JWT token is expired.
     *
     * @param token the JWT token to check
     * @return true if the token is expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Checks if a JWT token is present in the request header.
     *
     * @param header the request header containing the token
     * @return true if a JWT token is present, false otherwise
     */
    public boolean isTokenPresent(String header) {
        return header != null && header.startsWith("Bearer ");
    }

    /**
     * Extracts the subject (user email) from a JWT token.
     *
     * @param token the JWT token from which to extract the subject
     * @return the subject (user email) of the JWT token
     */
    public String extractJwtSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration date from a JWT token.
     *
     * @param token the JWT token from which to extract the expiration date
     * @return the expiration date of the JWT token
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from a JWT token using a claims resolver function.
     *
     * @param token          the JWT token from which to extract the claim
     * @param claimsResolver the function to apply to the claims to extract the desired information
     * @param <T>            the type of the extracted claim
     * @return the extracted claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);

        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from a JWT token.
     *
     * @param token the JWT token from which to extract the claims
     * @return the claims of the JWT token
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Retrieves and decodes the JWT secret key.
     *
     * @return the decoded secret key used for signing the JWT
     */
    private Key getSignInKey() {
        String jwtSecretKey = "a578ca941109363dff29882af80d0a4d28754a106ac96dc65c36092399175844";
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecretKey);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}
