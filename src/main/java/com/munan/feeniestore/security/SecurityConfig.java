package com.munan.feeniestore.security;

import com.munan.feeniestore.repository.AppUserRepository;
import com.munan.feeniestore.repository.OtpRepository;
import com.munan.feeniestore.security.filters.CustomeAuthenticationFilter;
import com.munan.feeniestore.security.filters.TokenAuthFilter;
import com.munan.feeniestore.security.providers.OtpAuthProvider;
import com.munan.feeniestore.security.providers.TokenAuthProvider;
import com.munan.feeniestore.security.providers.UsernamePasswordAuthProvider;
import com.munan.feeniestore.service.MyUserDetailService;
import com.munan.feeniestore.utils.TokenManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.List;

import static org.springframework.security.core.context.SecurityContextHolder.MODE_INHERITABLETHREADLOCAL;

@Configuration
@EnableWebSecurity
@EnableAsync
public class SecurityConfig {
    private final MyUserDetailService userDetailService;
    private final OtpAuthProvider otpAuthProvider;
    private final AppUserRepository appUserRepository;
    private final OtpRepository otpRepository;
    private final TokenManager tokenManager;


    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);


    public SecurityConfig(MyUserDetailService userDetailService,
                          OtpAuthProvider otpAuthProvider,
                          AppUserRepository appUserRepository,
                          OtpRepository otpRepository,
                          TokenManager tokenManager) {
        this.userDetailService = userDetailService;
        this.otpAuthProvider = otpAuthProvider;
        this.appUserRepository = appUserRepository;
        this.otpRepository = otpRepository;
        this.tokenManager = tokenManager;
    }

    @Bean
    AuthenticationManager authenticationManager(){

        log.info("STARTED AUTHENTICATION MANAGER");
        return new ProviderManager(
                List.of(usernamePasswordAuthProvider(),
                        otpAuthProvider,
                        tokenAuthProvider()));
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .addFilterAt(customeAuthenticationFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(tokenAuthFilter(), BasicAuthenticationFilter.class)
                .authorizeHttpRequests()
                .anyRequest().permitAll()
                .and()
                .build();
    }

    @Bean
    UsernamePasswordAuthProvider usernamePasswordAuthProvider(){
        return new UsernamePasswordAuthProvider(userDetailService, passwordEncoder());
    }
    @Bean
    TokenAuthProvider tokenAuthProvider(){
        return new TokenAuthProvider(tokenManager);
    }
    @Bean
    TokenAuthFilter tokenAuthFilter(){
        return new TokenAuthFilter(authenticationManager());
    }
    @Bean
    CustomeAuthenticationFilter customeAuthenticationFilter(){
        return new CustomeAuthenticationFilter(appUserRepository, otpRepository, authenticationManager(),tokenManager);
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public InitializingBean initializingBean(){
        return ()->{
            SecurityContextHolder.setStrategyName(MODE_INHERITABLETHREADLOCAL);
        };
    }
}
