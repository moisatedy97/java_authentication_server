package com.authentication.jas.database.repository;

import com.authentication.jas.database.entities.Otp;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Interface for the repository handling OTP (One-Time Password) entities.
 * This repository provides an abstraction layer to perform various operations on OTP data in the database.
 */
public interface OtpRepository extends JpaRepository<Otp, Integer> {

    /**
     * Retrieves an Optional container possibly containing the OTP associated with a specific user ID.
     * This method is used to fetch the OTP details for a user, if present.
     *
     * @param userId the unique identifier of the user whose OTP is to be retrieved
     * @return an Optional containing the OTP if it exists, otherwise an empty Optional
     */
    @NotNull
    Optional<Otp> findOtpByUserId(@NotNull Integer userId);
}
