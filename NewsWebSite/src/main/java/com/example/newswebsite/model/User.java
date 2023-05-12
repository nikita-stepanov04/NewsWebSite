package com.example.newswebsite.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue()
    private Long id;

    private String username;
    private String password;
    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    //statistics
    @Builder.Default private int politicsNewsViewedCounter = 0;
    @Builder.Default private int businessNewsViewedCounter = 0;
    @Builder.Default private int sportsNewsViewedCounter = 0;
    @Builder.Default private int entertainmentNewsViewedCounter = 0;
    @Builder.Default private int technologyNewsViewedCounter = 0;
    @Builder.Default private int healthNewsViewedCounter = 0;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
