package com.example.newswebsite.initialization;

import com.example.newswebsite.model.user.Role;
import com.example.newswebsite.model.user.User;
import com.example.newswebsite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminInitializer(UserRepository userRepository,
                            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        // check if admin exists in database before the server start
        // if not exists add admin

        try {
            userRepository.findByRole(Role.ADMIN)
                    .orElseThrow(() -> new Exception("Admin is not found"));

        } catch (Exception e) {
            User admin = User.builder()
                    .name("Admin")
                    .username("admin")
                    .password(passwordEncoder.encode("admin"))
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(admin);
        }
    }
}
