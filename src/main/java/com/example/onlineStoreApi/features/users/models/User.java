package com.example.onlineStoreApi.features.users.models;

import com.example.onlineStoreApi.features.users.utils.UserRoles;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Arrays;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor
public class User implements Serializable {
    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Long id;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String email;
    private String password;

    // @Transient // Not Database column
    private Boolean isActive = true;

    @Enumerated(EnumType.STRING)
    private UserRoles[] roles = {UserRoles.guest};

    public User(
            String firstName,
            String lastName,
            String email,
            String password,
            Boolean isActive,
            UserRoles[] roles
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.isActive = isActive;
        this.roles = roles;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", isActive=" + isActive +
                ", roles=" + Arrays.toString(roles) +
                '}';
    }
}
