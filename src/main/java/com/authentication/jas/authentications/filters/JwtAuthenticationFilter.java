package com.authentication.jas.authentications.filters;

import com.authentication.jas.authentications.UsernamePasswordAuthentication;
import com.authentication.jas.database.repository.TokenRepository;
import com.authentication.jas.services.JwtService;
import com.authentication.jas.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Custom JWT authentication filter class that extends {@link OncePerRequestFilter}.
 * This filter intercepts each request to validate JWT tokens and authenticate users based on the token's validity.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final UserService userService;

    /**
     * Processes each HTTP request to authenticate users based on JWT tokens.
     * It extracts the JWT token from the Authorization header, validates it, and sets the authentication in the security context.
     * If the token is invalid, it sets the HTTP response status to 401 Unauthorized.
     *
     * @param request     the HTTP request
     * @param response    the HTTP response
     * @param filterChain the filter chain
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (jwtService.isTokenPresent(authHeader)) {
            final String jwtToken = authHeader.substring(7);
            final String userEmail = jwtService.extractJwtSubject(jwtToken);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userService.loadUserByUsername(userEmail);

                boolean isTokenValid = tokenRepository.findByToken(jwtToken)
                        .map(token -> !token.isExpired() && !token.isRevoked())
                        .orElse(false);

                if (jwtService.isTokenValid(jwtToken, userDetails) && isTokenValid) {
                    UsernamePasswordAuthentication authentication = new UsernamePasswordAuthentication(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                    );

                    if (authentication.isAuthenticated() && jwtService.isTokenValid(jwtToken, userDetails)) {
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Determines whether the filter should be applied based on the request path.
     * This implementation skips filtering for login and registration paths.
     *
     * @param request the HTTP request
     * @return true if the filter should not be applied (i.e., the path is /auth/login or /auth/register), false otherwise
     * @throws ServletException if a servlet-specific error occurs
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().equals("/auth/login") ||
                request.getServletPath().equals("/auth/register");
    }
}
