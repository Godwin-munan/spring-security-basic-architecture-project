package com.munan.feeniestore.service;

import com.nimbusds.jose.proc.SecurityContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class AppUserService {
    public String getGreeting() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authentication Principal "+authentication.toString());
        System.out.println("Security Context: "+SecurityContextHolder.getContextHolderStrategy().toString());
        return "Hello World".toUpperCase()+" this is my auth principal "+authentication.getName();
    }
}
