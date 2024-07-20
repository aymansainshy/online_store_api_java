package com.example.onlineStoreApi.features.users.repositories;

import com.example.onlineStoreApi.features.users.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

//    @Query("SELECT u FROM users u WHERE u.email = ?1")
    Optional<User> findByEmail(String email);
}
