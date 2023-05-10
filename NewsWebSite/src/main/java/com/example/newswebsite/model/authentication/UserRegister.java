package com.example.newswebsite.model.authentication;

import com.example.newswebsite.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class UserRegister {

    @NotBlank(message = "Enter name")
    @Size(min = 5, message = "Minimal length is 3 characters")
    private String name;

    @NotBlank(message = "Enter username")
    @Size(min = 5, message = "Minimal length is 5 characters")
    private String username;

    @NotBlank(message = "Enter password")
    @Size(min = 6, message = "Minimal password length is 6 characters")
    private String password;

    @NotBlank(message = "Confirm password")
    @Size(min = 6, message = "Minimal password length is 6 characters")
    private String confirmPassword;
}
