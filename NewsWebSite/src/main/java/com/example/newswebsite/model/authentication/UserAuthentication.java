package com.example.newswebsite.model.authentication;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class UserAuthentication {
    private String username;
    private String password;
}
