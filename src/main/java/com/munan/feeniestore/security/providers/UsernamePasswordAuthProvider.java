package com.munan.feeniestore.security.providers;

import com.munan.feeniestore.security.authentication.UsernamePasswordAuthentication;
import com.munan.feeniestore.service.MyUserDetailService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

public class UsernamePasswordAuthProvider implements AuthenticationProvider {

    private final MyUserDetailService userDetailService;
    private final PasswordEncoder passwordEncoder;

    public UsernamePasswordAuthProvider(MyUserDetailService userDetailService, PasswordEncoder passwordEncoder) {
        this.userDetailService = userDetailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        UserDetails user = userDetailService.loadUserByUsername(username);

        if(passwordEncoder.matches(password, user.getPassword())){

            return new UsernamePasswordAuthentication(username, password, user.getAuthorities());
        }else{
            throw new BadCredentialsException("bad credentials");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthentication.class.equals(authentication);
    }
}
