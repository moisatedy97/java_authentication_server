package com.authentication.jas.database.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents the OTP (One-Time Password) entity associated with a specific user.
 * This entity is used to store and manage OTP data which includes the OTP code,
 * creation time, and expiration time.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Otp {

    /**
     * The unique identifier for the user associated with this OTP.
     * This is also used as a foreign key linked to the User entity.
     */
    @Id
    @Column(name = "user_id")
    private Integer id;

    /**
     * The user entity this OTP is associated with.
     * It maps directly to the user's ID, establishing a one-to-one relationship.
     */
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * The OTP code generated for the user.
     */
    private String otpCode;

    /**
     * The timestamp (in milliseconds since epoch) when the OTP was created.
     */
    private Long createdAt;

    /**
     * The timestamp (in milliseconds since epoch) when the OTP will expire.
     */
    private Long expiresAt;
}
