package com.authentication.jas.config;

import com.authentication.jas.authentications.providers.OtpAuthenticationProvider;
import com.authentication.jas.authentications.providers.UsernamePasswordAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebApplicationConfig {

    /**
     * Configures and builds the {@link AuthenticationManager} with specific authentication providers.
     * 
     * @param http the {@link HttpSecurity} used to get the shared {@link AuthenticationManagerBuilder}
     * @param usernamePasswordAuthenticationProvider the provider for username and password authentication
     * @param otpAuthenticationProvider the provider for OTP-based authentication
     * @return the fully configured {@link AuthenticationManager}
     * @throws Exception if there is an issue configuring the authentication manager
     */
    @Bean
    public AuthenticationManager authenticationManager(
            HttpSecurity http,
            UsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider,
            OtpAuthenticationProvider otpAuthenticationProvider
    ) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder.authenticationProvider(usernamePasswordAuthenticationProvider);
        authenticationManagerBuilder.authenticationProvider(otpAuthenticationProvider);

        return authenticationManagerBuilder.build();
    }

    /**
     * Creates a {@link PasswordEncoder} that uses the BCrypt strong hashing function.
     * 
     * @return the {@link PasswordEncoder} instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
