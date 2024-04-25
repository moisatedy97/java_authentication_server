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
 * Register user dto
 */
@Data
@AllArgsConstructor
public class RegisterUserDto {

    @NotNull
    @Size(min=3, max=30)
    private String firstName;
    @NotNull
    @Size(min=3, max=30)
    private String lastName;
    @Email
    @NotNull
    private String email;
    @NotNull
    @Size(min=6, max=30)
    private String password;
    private String birthDate;
    private String birthPlace;
    @Enumerated(EnumType.STRING)
    private Role role;
}
