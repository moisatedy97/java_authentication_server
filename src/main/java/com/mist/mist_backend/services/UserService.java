package com.mist.mist_backend.services;

import com.mist.mist_backend.constants.AuthenticationConstants;
import com.mist.mist_backend.database.entities.Otp;
import com.mist.mist_backend.database.entities.User;
import com.mist.mist_backend.database.repository.OtpRepository;
import com.mist.mist_backend.database.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Class that contains all user actions
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    @Value("${application.security.otp.longevity}")
    private long otpLongevity;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final OtpRepository otpRepository;
    private final OtpService otpService;
    private final EmailService emailService;


    /**
     * Function that creates/registers a User into the database
     *
     * @param user the user that we want to create
     */
    public void register(@NotNull User user) {
        String code = otpService.generateOtp();
        System.out.println(code);
        Otp otp = Otp.builder()
                .otpCode(passwordEncoder.encode(code))
                .user(user)
                .createdAt(System.currentTimeMillis())
                .expiresAt(System.currentTimeMillis() + (otpLongevity * 1000))
                .build();

        user.setOtp(otp);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
        renewUserOtp(user);
    }

    /**
     * Function that edits the user personal data
     * @param user the new user data
     */
    public void edit(@NotNull User user) {
        Optional<User> dbUser = userRepository.findUserByEmail(user.getEmail());

        if (dbUser.isPresent()) {
            User currentUser = dbUser.get();

            if (user.getFirstName() != null) {
                currentUser.setFirstName(user.getFirstName());
            }
            if (user.getLastName() != null) {
                currentUser.setLastName(user.getLastName());
            }
            if (user.getBirthDate() != null) {
                currentUser.setBirthDate(user.getBirthDate());
            }
            if (user.getBirthPlace() != null) {
                currentUser.setBirthPlace(user.getBirthPlace());
            }
        }
    }

    /**
     * Function that renews the otp of the found User
     *
     * @param user the user that we want to renew its otp
     */
    public void renewUserOtp(User user) {
        String code = otpService.generateOtp();

        Optional<Otp> dbOtp = otpRepository.findOtpByUserId(user.getId());

        if (dbOtp.isPresent()) {
            Otp currentOtp = dbOtp.get();
            System.out.println(code);
            currentOtp.setCreatedAt(System.currentTimeMillis());
            currentOtp.setExpiresAt(System.currentTimeMillis() + (otpLongevity * 1000));
            currentOtp.setOtpCode(passwordEncoder.encode(code));

            String recipient = user.getEmail();
            String subject = "Hello, " + user.getFirstName() + " " + user.getLastName() + " !";
            String template = "Hello, " + user.getFirstName() + "\n\n"
                    + "Here is you One-Time-Password: " + code + "\n\n"
                    + "We hope you're having a great day!\n\n"
                    + "Best regards,\n"
                    + "Mist";

            emailService.sendEmail(recipient, subject, template);
        }
    }

    /**
     * Function that loads the user by email from the database
     *
     * @param username the username of the user details
     * @return the user details found in the database
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> dbUser = userRepository.findUserByEmail(username);

        if (dbUser.isPresent()) {
            return dbUser.get();
        } else {
            throw new UsernameNotFoundException("Username not found: " + username);
        }
    }
}
