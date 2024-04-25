package com.authentication.jas.database.entities;

import com.authentication.jas.utils.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Represents the User entity in the database. This class implements UserDetails to provide
 * authentication details to Spring Security.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    /**
     * The primary key of the User entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The first name of the user.
     */
    private String firstName;

    /**
     * The last name of the user.
     */
    private String lastName;

    /**
     * The email of the user, used as the username in UserDetails. It must be unique.
     */
    @Column(unique = true)
    private String email;

    /**
     * The password of the user, used for authentication.
     */
    private String password;

    /**
     * The birth date of the user.
     */
    private String birthDate;

    /**
     * The birth place of the user.
     */
    private String birthPlace;

    /**
     * The role of the user, used to grant authorities in UserDetails.
     */
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * The one-to-one mapping to the Otp entity. Each user has one Otp.
     */
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Otp otp;

    /**
     * Returns the authorities granted to the user, based on the user's role.
     * @return a collection of GrantedAuthority objects.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    /**
     * Returns the username used to authenticate the user. Here, it's the email.
     * @return the email of the user.
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Returns the password used to authenticate the user.
     * @return the password of the user.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Indicates whether the user's account is enabled.
     * @return true, indicating the account is always enabled.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Indicates whether the user's account is expired.
     * @return true, indicating the account is never considered expired.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user's account is locked.
     * @return true, indicating the account is never locked.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) are expired.
     * @return true, indicating the credentials are never considered expired.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
