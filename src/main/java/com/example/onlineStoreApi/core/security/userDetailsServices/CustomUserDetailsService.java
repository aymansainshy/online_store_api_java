package com.example.onlineStoreApi.core.security.userDetailsServices;

import com.example.onlineStoreApi.core.exceptions.customeExceptions.ResourceNotFoundException;
import com.example.onlineStoreApi.features.users.models.User;
import com.example.onlineStoreApi.features.users.repositories.UserRepository;
import com.example.onlineStoreApi.services.cache.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final CacheService cacheService;

    @Autowired
    public CustomUserDetailsService(
            UserRepository userRepository,
            @Qualifier("InMemoryCache") CacheService cacheService
    ) {
        this.userRepository = userRepository;
        this.cacheService = cacheService;
    }


    // This provides new instance of UserDetailsService e with custom implementation to LoadUserByUsername.
    @Override
    public AppUserDetails loadUserByUsername(String username) throws ResourceNotFoundException {

        // Try to fetch user form cache if not then fetched from Repository.
        User cachedUser = (User) cacheService.get(username);

        if (cachedUser != null) {
            System.out.println("___-_____--_------_ CACHED USER----_-_---_--_-_-_---_-_-__-__ " + true);
            return AppUserDetails
                    .builder()
                    .id(cachedUser.getId())
                    .email(cachedUser.getEmail())
                    .password(cachedUser.getPassword())
                    .roles(cachedUser.getRoles())
                    .isActive(cachedUser.getIsActive())
                    .build();
        } else {
            // if not cached fetched from Repository and cached .
            User user = userRepository
                    .findByEmail(username)
                    .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

            System.out.println("___-_____--_------_Preform CACHe USER----_-_---_--_-_-_---_-_-__-__ ");
            cacheService.put(user.getEmail(), user);


//            org.springframework.security.core.userdetails.User.builder().authorities();

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
}
