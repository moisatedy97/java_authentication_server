package com.authentication.jas.constants;

import org.springframework.beans.factory.annotation.Value;

/**
 * Holds the constants used for authentication purposes within the application.
 */
public class AuthenticationConstants {

    /**
     * The longevity of the OTP (One-Time Password) in seconds.
     */
    public final static Integer OTP_LONGEVITY = 60;

    /**
     * The secret key used for signing and verifying JWT (JSON Web Tokens).
     */
    public static String JWT_TOKEN_SECRET_KEY = "a578ca941109363dff29882af80d0a4d28754a106ac96dc65c36092399175844";

    /**
     * The longevity of the JWT access token in seconds.
     */
    public static Long JWT_TOKEN_LONGEVITY = 3600L;

    /**
     * The longevity of the JWT refresh token in seconds.
     */
    public static Long JWT_REFRESH_TOKEN_LONGEVITY = 86400L;
}
