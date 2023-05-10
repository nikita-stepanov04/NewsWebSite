package com.example.newswebsite.controllers;

import com.example.newswebsite.model.authentication.UserAuthentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationController {

    private final UserAuthentication userAuthentication;

    public AuthenticationController(UserAuthentication userAuthentication) {
        this.userAuthentication = userAuthentication;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("userAuthentication", userAuthentication);
        return "authentication/login";
    }
}
