package com.example.newswebsite.controllers;

import com.example.newswebsite.model.Role;
import com.example.newswebsite.model.User;
import com.example.newswebsite.model.authentication.UserAuthentication;
import com.example.newswebsite.model.authentication.UserRegister;
import com.example.newswebsite.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthenticationController {

    private final UserAuthentication userAuthentication;
    private final UserRegister userRegister;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public AuthenticationController(UserAuthentication userAuthentication,
                                    UserRegister userRegister,
                                    UserRepository userRepository,
                                    PasswordEncoder encoder) {
        this.userAuthentication = userAuthentication;
        this.userRegister = userRegister;
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("userAuthentication", userAuthentication);
        return "authentication/login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("userRegister", userRegister);
        return "authentication/register";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute @Valid UserRegister userRegister,
                                  Errors errors, Model model) {

        if (!userRegister.getPassword().equals(userRegister.getPasswordConfirmation())) {
            model.addAttribute("passwordsNotEqual", "Passwords are not equal");
        }

        if (userRepository.findByUsername(userRegister.getUsername()).orElse(null) != null) {
            model.addAttribute("usernameIsTaken", "Username is already taken");
        }

        if (errors.hasErrors()
                || model.containsAttribute("passwordsNotEqual")
                || model.containsAttribute("usernameIsTaken")
        ) {
            System.out.println(userRegister);
            return "authentication/register";
        }

        var user = User.builder()
                .name(userRegister.getName())
                .username(userRegister.getUsername())
                .password(encoder.encode(userRegister.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        return "redirect:/login";
    }
}
