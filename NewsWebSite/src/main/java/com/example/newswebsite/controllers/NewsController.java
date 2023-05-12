package com.example.newswebsite.controllers;

import com.example.newswebsite.model.NewsType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;

@Controller
@RequestMapping("/news")
public class NewsController {

    @GetMapping
    public String news(Model model) {
        // todo брать тип товости из бд
        model.addAttribute("imagePath", "img/sports_news.png");
        model.addAttribute("currantNewsType", "entertainment");
        model.addAttribute("newsTypes",
                Arrays.stream(NewsType.values()).map(type -> type.toString().toLowerCase()));
        return "news/homepage";
    }

}
