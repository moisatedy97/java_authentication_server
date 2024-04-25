package com.authentication.jas.services;

import com.authentication.jas.database.entities.Otp;
import com.authentication.jas.database.entities.User;
import com.authentication.jas.database.repository.OtpRepository;
import com.authentication.jas.database.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
     * Registers a new user in the database and sends an OTP to the user's email.
     * The password and OTP code are encoded before storage.
     *
     * @param user the user to be registered
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
     * Updates the personal data of an existing user in the database.
     * Only non-null fields in the input user object are updated.
     *
     * @param user the user with updated data
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
     * Renews the OTP for a user and sends a new OTP via email.
     * The new OTP is generated, encoded, and updated in the database.
     *
     * @param user the user whose OTP needs to be renewed
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
                    + "\n"
                    + "Have a nice day!\n\n";

            emailService.sendEmail(recipient, subject, template);
        }
    }

    /**
     * Loads a user's details by their email address.
     * Throws UsernameNotFoundException if the user is not found.
     *
     * @param username the email of the user to load
     * @return UserDetails of the found user
     * @throws UsernameNotFoundException if no user is found with the given email
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
