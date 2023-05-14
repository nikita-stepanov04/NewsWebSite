package com.example.newswebsite.controllers;

import com.example.newswebsite.model.user.User;
import com.example.newswebsite.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/myAccount")
public class UserAccountController {
    private User userData;
    private final UserRepository userRepository;

    @Autowired
    public UserAccountController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping(("/info"))
    public String myAccount(Model model) {

        // I use userData in all methods of the UserAccountController, so I made the userData a class variable
        // in order to take in from the database once and then use everywhere
        if (userData == null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            userData = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User was not found"));
        }

        model.addAttribute("userData", userData);
        model.addAttribute("currantOption", "info");
        return "myAccount/my-account-info";
    }
}
