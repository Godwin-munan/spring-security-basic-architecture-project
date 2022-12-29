package com.munan.feeniestore.controller;

import com.munan.feeniestore.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HomeResource {
    private final AppUserService userService;

    @GetMapping("/async/hello")
    @Async
    public void asyncGreeting(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authentication Principal "+authentication.toString());
    }

    @GetMapping("/hello")
    public String greeting(){

        return userService.getGreeting();
    }
}
