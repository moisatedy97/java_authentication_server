package com.authentication.jas.dto.request;

import com.authentication.jas.utils.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data Transfer Object for registering a new user.
 * This class is used to encapsulate the necessary data for user registration,
 * ensuring that all required fields meet the specified constraints.
 */
@Data
@AllArgsConstructor
public class RegisterUserDto {

    /**
     * The first name of the user.
     * Must be between 3 and 30 characters in length.
     */
    @NotNull
    @Size(min=3, max=30)
    private String firstName;

    /**
     * The last name of the user.
     * Must be between 3 and 30 characters in length.
     */
    @NotNull
    @Size(min=3, max=30)
    private String lastName;

    /**
     * The email address of the user.
     * Must be a valid email format and cannot be null.
     */
    @Email
    @NotNull
    private String email;

    /**
     * The password for the user's account.
     * Must be between 6 and 30 characters in length.
     */
    @NotNull
    @Size(min=6, max=30)
    private String password;

    /**
     * The birth date of the user.
     * This field is optional.
     */
    private String birthDate;

    /**
     * The birth place of the user.
     * This field is optional.
     */
    private String birthPlace;

    /**
     * The role assigned to the user.
     * Stored as a string in the database.
     */
    @Enumerated(EnumType.STRING)
    private Role role;
}
