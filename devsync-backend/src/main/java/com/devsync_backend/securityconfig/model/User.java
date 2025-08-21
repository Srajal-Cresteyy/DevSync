package com.devsync_backend.securityconfig.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "UserTable")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String emailAddress;

    @Column
    private String userName;

    @Column
    private String password;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "user_role", columnDefinition = "text[]")
    private String[] userRole;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays
                .stream(userRole)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    @Override
    public @Nullable String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }
}
