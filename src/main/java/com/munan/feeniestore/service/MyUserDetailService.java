package com.munan.feeniestore.service;

import com.munan.feeniestore.model.AppUser;
import com.munan.feeniestore.repository.AppUserRepository;
import com.munan.feeniestore.utils.MyUserDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailService implements UserDetailsService {
    private final AppUserRepository appUserRepository;
    private static final Logger log = LoggerFactory.getLogger(MyUserDetailService.class);

    public MyUserDetailService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("CALLED");
        Optional<AppUser> user = appUserRepository.findAppUserByUsername(username);

        AppUser appUser = user.orElseThrow(()-> new UsernameNotFoundException("Username not found"));

        return new MyUserDetail(appUser);
    }
}
