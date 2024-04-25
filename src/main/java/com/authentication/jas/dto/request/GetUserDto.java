package com.authentication.jas.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data Transfer Object for retrieving a user by email.
 * This class is used to encapsulate the email address as a part of the request to fetch user details.
 */
@Data
@AllArgsConstructor
public class GetUserDto {

    /**
     * The email address of the user.
     * Must be a valid email format and cannot be null.
     */
    @Email
    @NotNull
    private String email;
}
