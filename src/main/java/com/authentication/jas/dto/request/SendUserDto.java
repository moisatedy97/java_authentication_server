package com.authentication.jas.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendUserDto {

    @Email
    @NotNull
    private String email;
    @Size(min=3, max=30)
    private String firstName;
    @Size(min=3, max=30)
    private String lastName;
    @Size(min=3, max=30)
    private String birthDate;
    @Size(min=3, max=30)
    private String birthPlace;
}
