package com.example.newswebsite.controllers;

import com.example.newswebsite.model.news.NewsType;
import com.example.newswebsite.model.user.User;
import com.example.newswebsite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.Map;

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

        getUserData();

        model.addAttribute("userData", userData);
        model.addAttribute("currantOption", "info");
        return "myAccount/my-account-info";
    }

    @GetMapping("/stats")
    public String statistics(Model model) {

        getUserData();

        Map<String, Integer> userStats = userData.getNewsTypeAndTheirCountersMap();
        int favouriteNewsTypeCounterValue = userStats.get(userData.getFavouriteNewsType());

        model.addAttribute("keySet", userStats.keySet());
        model.addAttribute("values", userStats.values());
        model.addAttribute("maxValue", favouriteNewsTypeCounterValue);
        model.addAttribute("currantOption", "stats");
        model.addAttribute("newsTypes",
                Arrays.stream(NewsType.values()).map(type -> type.toString().toLowerCase()));

        return "myAccount/my-account-stats";
    }

    @GetMapping("/history")
    public String history(Model model) {

        getUserData();

        model.addAttribute("currantOption", "history");
        return "myAccount/my-account-history";
    }

    private void getUserData() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        userData = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User was not found"));
    }
}
