package com.munan.feeniestore.repository;

import com.munan.feeniestore.model.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Long> {

    Optional<Otp> findOtpByUser_Username(String username);
    Optional<Otp> findOtpByOtp(String otp);


}
