package com.example.onlineStoreApi.features.users.models;

import com.example.onlineStoreApi.features.users.utils.UserRoles;
import jakarta.persistence.*;

import lombok.*;

import java.io.Serializable;
import java.util.Arrays;

@Entity
@Table(name = "users")
@Data // == @Getter @Setter and more
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
}
