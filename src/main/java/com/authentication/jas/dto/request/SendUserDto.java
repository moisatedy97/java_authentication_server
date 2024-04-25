package com.authentication.jas.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for sending user details.
 * This class is used to encapsulate the necessary data for sending user information,
 * ensuring that all required fields meet the specified constraints.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendUserDto {

    /**
     * The email address of the user.
     * Must be a valid email format and cannot be null.
     */
    @Email
    @NotNull
    private String email;

    /**
     * The first name of the user.
     * Must be between 3 and 30 characters in length.
     */
    @Size(min=3, max=30)
    private String firstName;

    /**
     * The last name of the user.
     * Must be between 3 and 30 characters in length.
     */
    @Size(min=3, max=30)
    private String lastName;

    /**
     * The birth date of the user.
     * Must be between 3 and 30 characters in length.
     */
    @Size(min=3, max=30)
    private String birthDate;

    /**
     * The birth place of the user.
     * Must be between 3 and 30 characters in length.
     */
    @Size(min=3, max=30)
    private String birthPlace;
}
