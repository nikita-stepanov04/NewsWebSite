package com.example.newswebsite.repository;

import com.example.newswebsite.model.user.Role;
import com.example.newswebsite.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findFirstByRole(Role role);
}