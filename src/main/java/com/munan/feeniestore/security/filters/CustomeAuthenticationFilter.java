package com.munan.feeniestore.security.filters;

import com.munan.feeniestore.model.AppUser;
import com.munan.feeniestore.model.Otp;
import com.munan.feeniestore.repository.AppUserRepository;
import com.munan.feeniestore.repository.OtpRepository;
import com.munan.feeniestore.security.authentication.OtpAuthentication;
import com.munan.feeniestore.security.authentication.UsernamePasswordAuthentication;
import com.munan.feeniestore.utils.TokenManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

//@Component
public class CustomeAuthenticationFilter extends OncePerRequestFilter {

    private  AppUserRepository userRepository;
    private  OtpRepository otpRepository;
    private  AuthenticationManager manager;

    private  TokenManager tokenManager;

    public CustomeAuthenticationFilter(AppUserRepository userRepository,
                                       OtpRepository otpRepository,
                                       AuthenticationManager manager,
                                       TokenManager tokenManager) {
        this.userRepository = userRepository;
        this.otpRepository = otpRepository;
        this.manager = manager;
        this.tokenManager = tokenManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain){

        String username = request.getHeader("username");
        String password = request.getHeader("password");
        String otp = request.getHeader("otp");

        try{

            if(otp == null){

                Optional<AppUser> findUser = userRepository.findAppUserByUsername(username);
                AppUser user = findUser.orElseThrow(()-> new UsernameNotFoundException("Bad Credential"));

                Authentication auth = new UsernamePasswordAuthentication(user.getUsername(),password);
                manager.authenticate(auth);

                String code = String.valueOf(new Random().nextInt(9999) + 10000);

                Otp otpEntity = new Otp();
                otpEntity.setOtp(code);
                otpEntity.setUser(user);
                otpRepository.save(otpEntity);
            }else{

                Authentication auth = new OtpAuthentication(username, otp);
                manager.authenticate(auth);

                String token = UUID.randomUUID().toString();
                tokenManager.add(token);
                response.setHeader("Authorization", token);

            }

        }catch (AuthenticationException e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setHeader("Authentication Error", e.getMessage());
        }

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request){
        return !request.getServletPath().equals("/api/login");
    }
}
