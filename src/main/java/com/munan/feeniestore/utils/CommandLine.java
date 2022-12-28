package com.munan.feeniestore.utils;

import com.munan.feeniestore.model.AppUser;
import com.munan.feeniestore.repository.AppUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CommandLine implements CommandLineRunner {
    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static  final Logger log = LoggerFactory.getLogger(CommandLine.class);

    public CommandLine(AppUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        try{
            Optional<AppUser> user = userRepository.findAppUserByUsername("admin@gmail.com");

            if(user.isPresent()){
                throw new Exception("User already exist");
            }
            AppUser appUser = new AppUser();
            appUser.setUsername("admin@gmail.com");
            appUser.setPassword(passwordEncoder.encode("1234"));
            userRepository.save(appUser);
        }catch (Exception e){
            log.info("'{}'",e.getMessage());
        }

    }
}
