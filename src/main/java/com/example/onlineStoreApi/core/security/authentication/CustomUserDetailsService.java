package com.example.onlineStoreApi.core.security.authentication;

import com.example.onlineStoreApi.features.users.models.User;
import com.example.onlineStoreApi.features.users.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;


    // This provides new instance of UserDetailsService e with custom implementation to LoadUserByUsername.
    @Override
    public AppUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // TODO: Try to fetch user form cache if not then fetched from Repository..

        User user = userRepository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));


        return AppUserDetails
                .builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRoles())
                .isActive(user.getIsActive())
                .build();
    }
}
