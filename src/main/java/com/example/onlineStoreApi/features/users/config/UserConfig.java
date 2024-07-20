package com.example.onlineStoreApi.features.users.config;

import com.example.onlineStoreApi.features.users.models.User;
import com.example.onlineStoreApi.features.users.repositories.UserRepository;
import com.example.onlineStoreApi.features.users.utils.UserRoles;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class UserConfig {

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository) {
        UserRoles[] aymanRoles = {UserRoles.admin, UserRoles.staff};
        UserRoles[] sohaibRoles = {UserRoles.staff};
        UserRoles[] adamRoles = {UserRoles.guest};

        return args -> {

            User ayman = new User(
                    "Ayman",
                    "Abdulrahman",
                    "ayman@gmail.com",
                    "password",
                    true,
                    aymanRoles
            );


            User sohaib = new User(
                    "Sohaib",
                    "Badawe",
                    "sohaib@gmail.com",
                    "password",
                    true,
                    adamRoles
            );


            User adam = new User(
                    "Adam",
                    "Mohammed",
                    "adam@gmail.com",
                    "password",
                    true,
                    sohaibRoles
            );


            userRepository.saveAll(List.of(ayman, adam, sohaib));
        };
    }
}
