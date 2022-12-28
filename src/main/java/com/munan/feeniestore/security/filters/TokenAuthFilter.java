package com.munan.feeniestore.security.filters;

import com.munan.feeniestore.security.authentication.TokenAuthentication;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class TokenAuthFilter extends OncePerRequestFilter {

    private final AuthenticationManager manager;

    public TokenAuthFilter(AuthenticationManager manager) {
        this.manager = manager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = request.getHeader("Authorization");

        Authentication auth = new TokenAuthentication(token, null);

        try{

            Authentication authentication = manager.authenticate(auth);

            SecurityContextHolder.getContext().setAuthentication(authentication);

        }catch(AuthenticationException e){
            response.setHeader("Error", e.getMessage());
        }

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request){
        return request.getServletPath().equals("/api/login");
    }
}
