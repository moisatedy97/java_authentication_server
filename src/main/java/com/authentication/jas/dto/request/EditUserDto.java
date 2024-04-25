package com.authentication.jas.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data Transfer Object for editing user details.
 * This class is used to transfer user data for edit operations, ensuring that the necessary constraints are met.
 */
@Data
@AllArgsConstructor
public class EditUserDto {

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
