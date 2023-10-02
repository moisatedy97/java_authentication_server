package com.mist.mist_backend.dto.request;

import com.mist.mist_backend.utils.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
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
