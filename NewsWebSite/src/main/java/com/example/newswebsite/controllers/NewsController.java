package com.example.newswebsite.controllers;

import com.example.newswebsite.model.NewsType;
import com.example.newswebsite.model.User;
import com.example.newswebsite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;

@Controller
@RequestMapping("/news")
public class NewsController {

    private final UserRepository userRepository;

    @Autowired
    public NewsController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String news(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User userInfo = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User was not found"));

        return "redirect:/news/" + userInfo.getFavouriteNewsType();
    }

    @GetMapping("/{newsType}")
    public String allNewsByType(@PathVariable("newsType") String newsType,
                                Model model) {

        model.addAttribute("currantNewsType", newsType);
        model.addAttribute("newsImagePath", "/img/" + newsType + "_news.png");
        model.addAttribute("newsTypes",
                Arrays.stream(NewsType.values()).map(type -> type.toString().toLowerCase()));



        return "news/news";
    }

}
