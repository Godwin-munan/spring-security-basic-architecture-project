package com.munan.feeniestore.security.providers;

import com.munan.feeniestore.security.authentication.TokenAuthentication;
import com.munan.feeniestore.utils.TokenManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class TokenAuthProvider implements AuthenticationProvider {

    private final TokenManager tokenManager;

    public TokenAuthProvider(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String token = authentication.getName();
        if(tokenManager.contains(token)){
            return new TokenAuthentication(token, null, null);
        }

       throw new BadCredentialsException("Bad Token");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return TokenAuthentication.class.equals(authentication);
    }
}
