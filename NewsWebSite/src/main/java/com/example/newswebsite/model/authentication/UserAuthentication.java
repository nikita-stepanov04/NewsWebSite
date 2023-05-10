package com.example.newswebsite.model.authentication;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class UserAuthentication {

    @NotBlank(message = "Enter username")
    @Size(min = 5, message = "Minimal length is 5 characters")
    private String username;

    @NotBlank(message = "Enter password")
    @Size(min = 6, message = "Minimal password length is 6 characters")
    private String password;
}
