package com.authentication.jas.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Edit user dto
 */
@Data
@AllArgsConstructor
public class EditUserDto {

    @Size(min=3, max=30)
    private String firstName;
    @Size(min=3, max=30)
    private String lastName;
    @Size(min=3, max=30)
    private String birthDate;
    @Size(min=3, max=30)
    private String birthPlace;
}
