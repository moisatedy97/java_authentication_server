package com.authentication.jas.controllers;

import com.authentication.jas.database.entities.User;
import com.authentication.jas.database.repository.UserRepository;
import com.authentication.jas.dto.request.SendUserDto;
import com.authentication.jas.services.UserService;
import com.authentication.jas.dto.request.EditUserDto;
import com.authentication.jas.dto.request.GetUserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/**
 * Controller for handling various demo operations.
 */
@RestController
@RequiredArgsConstructor
public class DemoController {

    @Value("${com.authentication.jas.version}")
    private String version;
    private final UserRepository userRepository;
    private final UserService userService;

    /**
     * Endpoint to retrieve the current version of the application.
     * @return The current version as a string.
     */
    @GetMapping("/ver")
    public String ver() {
        return version;
    }

    /**
     * Endpoint to retrieve user details based on the provided email.
     * @param getUserDto Data transfer object containing the email of the user to be retrieved.
     * @return ResponseEntity containing the user details or an error message if not found.
     */
    @GetMapping("/user")
    public ResponseEntity<SendUserDto> user(@Valid GetUserDto getUserDto) {
        try {
            Optional<User> dbUser = userRepository.findUserByEmail(getUserDto.getEmail());

            if (dbUser.isPresent()) {
                User currentUser = dbUser.get();

                SendUserDto sendUserDto = SendUserDto.builder()
                        .email(getUserDto.getEmail())
                        .firstName(currentUser.getFirstName())
                        .lastName(currentUser.getLastName())
                        .birthDate(currentUser.getBirthDate())
                        .birthPlace(currentUser.getBirthPlace())
                        .build();

                return new ResponseEntity<SendUserDto>(sendUserDto, HttpStatus.OK);
            } else {
                throw new BadCredentialsException("User not found!");
            }
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Resource Not Found", e);
        }
    }

    /**
     * Endpoint to edit user details based on the authenticated user's email.
     * @param editUserDto Data transfer object containing the new details of the user.
     * @param authentication Authentication object containing the current user's details.
     */
    @GetMapping("/edit")
    public void edit(@Valid EditUserDto editUserDto, Authentication authentication) {

        User user = User.builder()
                .email(authentication.getName())
                .firstName(editUserDto.getFirstName())
                .lastName(editUserDto.getLastName())
                .birthDate(editUserDto.getBirthDate())
                .birthPlace(editUserDto.getBirthPlace())
                .build();

        userService.edit(user);
    }

    /**
     * Endpoint to check the validity of the current user's token.
     */
    @GetMapping("/check")
    public void checkToken() {}
}
