package com.authentication.jas.services;

import com.authentication.jas.authentications.OtpAuthentication;
import com.authentication.jas.authentications.UsernamePasswordAuthentication;
import com.authentication.jas.database.entities.Token;
import com.authentication.jas.database.entities.User;
import com.authentication.jas.database.repository.TokenRepository;
import com.authentication.jas.utils.TokenType;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;

    /**
     * Authenticates a user based on their credentials. This method supports both username-password
     * and OTP-based authentication.
     *
     * @param user the user to authenticate, must not be null
     * @return the authenticated user's details as an Authentication object if authentication is successful, null otherwise
     */
    public Authentication authenticate(@NotNull User user) {
        if (user.getOtp().getOtpCode() == null && !user.getPassword().isEmpty()) {
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthentication(user.getEmail(), user.getPassword())
            );
        }

        if (user.getPassword() == null && !user.getOtp().getOtpCode().isEmpty()) {
            return authenticationManager.authenticate(
                    new OtpAuthentication(user.getEmail(), user.getOtp().getOtpCode())
            );
        }

        return null;
    }

    /**
     * Saves a JWT token for a user in the database. This token can be used for subsequent
     * authorization checks.
     *
     * @param user     the user for whom the token is being saved
     * @param jwtToken the JWT token to save
     */
    public void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();

        tokenRepository.save(token);
    }

    /**
     * Revokes all valid tokens for a user. This is typically used when a user logs out or when
     * their tokens need to be refreshed for security reasons.
     *
     * @param user the user whose tokens are to be revoked
     */
    public void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
