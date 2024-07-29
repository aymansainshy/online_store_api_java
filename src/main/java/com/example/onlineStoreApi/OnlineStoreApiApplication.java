package com.example.onlineStoreApi;

import com.example.onlineStoreApi.services.cache.InMemoryCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class OnlineStoreApiApplication {

    public static void main(String[] args) {
        System.out.println("Hello, Ayman Abdulrahman and Welcome to Spring Boot (<O^O>)");

        ApplicationContext context = SpringApplication.run(OnlineStoreApiApplication.class, args);

        // By default, Spring beans are singleton scoped,
        // meaning only one instance is created and shared across the application.
        InMemoryCache inMemoryCache1 = context.getBean(InMemoryCache.class);
        InMemoryCache inMemoryCache2 = context.getBean(InMemoryCache.class);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (inMemoryCache1 == inMemoryCache2));
    }
}



// users (
//        primary key (id)
//        first_name varchar(255),
//        last_name varchar(255),
//        email varchar(255) unique,
//        password varchar(255),
//        is_active boolean,
//        id bigint not null,
//        roles varchar(255) array,
//       )
