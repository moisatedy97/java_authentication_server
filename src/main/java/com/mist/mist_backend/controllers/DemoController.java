package com.mist.mist_backend.controllers;

import com.mist.mist_backend.database.entities.User;
import com.mist.mist_backend.database.repository.UserRepository;
import com.mist.mist_backend.dto.request.EditUserDto;
import com.mist.mist_backend.dto.request.GetUserDto;
import com.mist.mist_backend.dto.response.SendUserDto;
import com.mist.mist_backend.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class DemoController {

    @Value("${com.mist.mist_backend.version}")
    private String version;
    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping("/ver")
    public String ver() {
        return version;
    }

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

    @GetMapping("/check")
    public void checkToken() {}
}
