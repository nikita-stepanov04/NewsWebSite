package com.example.newswebsite.model.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    @Builder.Default public int politicsNewsViewedCounter = 0;
    @Builder.Default public int businessNewsViewedCounter = 0;
    @Builder.Default public int sportsNewsViewedCounter = 0;
    @Builder.Default public int entertainmentNewsViewedCounter = 0;
    @Builder.Default public int technologyNewsViewedCounter = 0;
    @Builder.Default public int healthNewsViewedCounter = 0;

    public Map<String, Integer> getNewsTypeAndTheirCountersMap() {
        Map<String, Integer> newsViewsCounterMap = new HashMap<>();
        newsViewsCounterMap.put("politics", politicsNewsViewedCounter);
        newsViewsCounterMap.put("business", businessNewsViewedCounter);
        newsViewsCounterMap.put("sports", sportsNewsViewedCounter);
        newsViewsCounterMap.put("entertainment", entertainmentNewsViewedCounter);
        newsViewsCounterMap.put("technology", technologyNewsViewedCounter);
        newsViewsCounterMap.put("health", healthNewsViewedCounter);
        return newsViewsCounterMap;
    }

    public String getFavouriteNewsType() {
        Map<String, Integer> newsViewsCounterMap = getNewsTypeAndTheirCountersMap();
        Map.Entry<String, Integer> favouriteNewsType = newsViewsCounterMap.entrySet()
                .stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .orElseThrow();
        return favouriteNewsType.getKey();
    }

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
