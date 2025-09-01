package com.example.onlineStoreApi.features.users.models;

import com.example.onlineStoreApi.features.users.utils.UserRoles;
import jakarta.persistence.*;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Data // == @Getter @Setter and more
@Builder
@NoArgsConstructor  // ✅ required by JPA
@AllArgsConstructor // ✅ works with @Builder
public class User { // public class User implements Serializable, UserDetails {}
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
    @Builder.Default
    private Boolean isActive = true;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private UserRoles[] roles = {UserRoles.guest};

}
