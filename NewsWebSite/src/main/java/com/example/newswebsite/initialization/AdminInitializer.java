package com.example.newswebsite.initialization;

import com.example.newswebsite.model.user.Role;
import com.example.newswebsite.model.user.User;
import com.example.newswebsite.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    @Autowired
    public AdminInitializer(UserRepository userRepository,
                            PasswordEncoder passwordEncoder,
                            ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) throws IOException {

        // check if admin exists in database before the server start
        // if not exists add admin

        if (userRepository.findFirstByRole(Role.ADMIN).isEmpty()) {
            Resource resource = new ClassPathResource("/json/admins.json");
            File adminsJsonFile = resource.getFile();

            AdminsContainer container = objectMapper.readValue(adminsJsonFile,
                    new TypeReference<>(){});

            List<User> admins = new LinkedList<>();

            for (User admin: container.getAdminsList()) {
                var user = User.builder()
                        .name(admin.getName())
                        .role(Role.ADMIN)
                        .username(admin.getUsername())
                        .password(passwordEncoder.encode(admin.getPassword()))
                        .build();
                admins.add(user);
            }
            userRepository.saveAll(admins);
        }
    }
}
