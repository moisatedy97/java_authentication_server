package com.authentication.jas.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for authentication response.
 * This class is used to encapsulate the access token and refresh token received upon successful authentication.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRes {

    /**
     * The access token that can be used to access secured resources.
     */
    @JsonProperty("access_token")
    private String accessToken;

    /**
     * The refresh token that can be used to renew the access token when it expires.
     */
    @JsonProperty("refresh_token")
    private String refreshToken;
}
