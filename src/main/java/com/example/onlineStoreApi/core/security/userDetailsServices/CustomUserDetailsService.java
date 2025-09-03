package com.example.onlineStoreApi.core.security.userDetailsServices;

import com.example.onlineStoreApi.core.exceptions.customeExceptions.ResourceNotFoundException;
import com.example.onlineStoreApi.core.utils.StructuredLogger;
import com.example.onlineStoreApi.features.authentication.services.AuthServiceImpl;
import com.example.onlineStoreApi.features.users.models.User;
import com.example.onlineStoreApi.features.users.repositories.UserRepository;
import com.example.onlineStoreApi.services.cache.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class CustomUserDetailsService implements UserDetailsService {
    StructuredLogger logger = StructuredLogger.getLogger(CustomUserDetailsService.class);

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
            logger.debug("CACHED_USER_FOUND", Map.of("email", cachedUser.getEmail()));
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

            logger.debug("PERFORM_CACHED_USER", Map.of("email", user.getEmail()));
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
