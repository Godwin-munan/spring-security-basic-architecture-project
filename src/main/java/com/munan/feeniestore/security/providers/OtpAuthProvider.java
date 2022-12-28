package com.munan.feeniestore.security.providers;

import com.munan.feeniestore.repository.OtpRepository;
import com.munan.feeniestore.security.authentication.OtpAuthentication;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OtpAuthProvider implements AuthenticationProvider {

    private final OtpRepository otpRepository;

    public OtpAuthProvider(OtpRepository otpRepository) {
        this.otpRepository = otpRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String otp = (String) authentication.getCredentials();

//        var existOtp = otpRepository.findOtpByUser_Username(username);
        var otpExist = otpRepository.findOtpByOtp(otp).orElseThrow(()->new BadCredentialsException("Bad OTP"));

        if(otpExist != null){

            return new OtpAuthentication(username,otpExist.getOtp(), List.of(()->"read"));

        }

        throw new BadCredentialsException("Bad Credentials");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OtpAuthentication.class.equals(authentication);
    }
}
