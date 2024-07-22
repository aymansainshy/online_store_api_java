package com.example.onlineStoreApi.features.users.config;

import com.example.onlineStoreApi.features.users.models.User;
import com.example.onlineStoreApi.features.users.repositories.UserRepository;
import com.example.onlineStoreApi.features.users.utils.UserRoles;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@AllArgsConstructor
public class UserConfig {

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository) {
        UserRoles[] aymanRoles = {UserRoles.admin, UserRoles.staff};
        UserRoles[] sohaibRoles = {UserRoles.staff};
        UserRoles[] adamRoles = {UserRoles.guest};

        return args -> {

            User ayman = User.builder()
                    .firstName("Ayman")
                    .lastName("Abdulrahman")
                    .email("ayman@gmail.com")
                    .password(passwordEncoder.encode("password"))
                    .isActive(true)
                    .roles(aymanRoles)
                    .build();

            User sohaib = User.builder()
                    .firstName("Sohaib")
                    .lastName("Badawe")
                    .email("sohaib@gmail.com")
                    .password(passwordEncoder.encode("password"))
                    .isActive(true)
                    .roles(sohaibRoles)
                    .build();

            User adam = User.builder()
                    .firstName("Adam")
                    .lastName("Mohammed")
                    .email("adam@gmail.com")
                    .password(passwordEncoder.encode("password"))
                    .isActive(true)
                    .roles(adamRoles)
                    .build();

            userRepository.saveAll(List.of(ayman, sohaib, adam));
        };
    }
}
