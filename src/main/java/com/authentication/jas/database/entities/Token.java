package com.authentication.jas.database.entities;

import com.authentication.jas.utils.TokenType;
import jakarta.persistence.*;
import lombok.*;

/**
 * Represents an authentication token used in the system.
 * This entity is linked to a specific user and can be used to manage access and authentication states.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {

    /**
     * The unique identifier for the token.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    /**
     * The token string which is unique across all tokens.
     */
    @Column(unique = true)
    public String token;

    /**
     * The type of the token, which defines the context in which the token can be used.
     * Default is TokenType.BEARER.
     */
    @Enumerated(EnumType.STRING)
    public TokenType tokenType = TokenType.BEARER;

    /**
     * Flag indicating whether the token has been revoked.
     */
    public boolean revoked;

    /**
     * Flag indicating whether the token has expired.
     */
    public boolean expired;

    /**
     * The user to whom the token is associated.
     * This is a many-to-one relationship as a user can have multiple tokens.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User user;
}
