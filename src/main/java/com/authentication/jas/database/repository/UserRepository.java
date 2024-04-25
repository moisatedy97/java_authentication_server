package com.authentication.jas.database.repository;

import com.authentication.jas.database.entities.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Interface for the repository handling User entities.
 * This repository provides an abstraction layer to perform various operations on User data in the database.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Retrieves an Optional container possibly containing the User associated with a specific email.
     * This method is used to fetch the User details based on the email, if present.
     *
     * @param email the email of the user to be retrieved
     * @return an Optional containing the User if found, otherwise an empty Optional
     */
    @NotNull
    Optional<User> findUserByEmail(@NotNull String email);

}
