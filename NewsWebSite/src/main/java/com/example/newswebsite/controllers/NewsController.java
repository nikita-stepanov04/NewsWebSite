package com.example.newswebsite.controllers;

import com.example.newswebsite.model.news.FullNews;
import com.example.newswebsite.model.news.News;
import com.example.newswebsite.model.news.NewsType;
import com.example.newswebsite.model.user.User;
import com.example.newswebsite.repository.NewsRepository;
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
import java.util.Date;

@Controller
@RequestMapping("/news")
public class NewsController {

    private final UserRepository userRepository;
    private final NewsRepository newsRepository;

    @Autowired
    public NewsController(UserRepository userRepository,
                          NewsRepository newsRepository) {
        this.userRepository = userRepository;
        this.newsRepository = newsRepository;
    }

    @GetMapping
    public String news() {

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
        model.addAttribute("newsTypes",
                Arrays.stream(NewsType.values()).map(type -> type.toString().toLowerCase()));
        model.addAttribute("newsPreviews", newsRepository.getNewsPreviewsByNewsType(newsType));

        return "news/news";
    }

    @GetMapping("/{newsType}/{newsId}")
    public String newsByID(@PathVariable("newsType") String newsType,
                           @PathVariable("newsId") Long id,
                           Model model) {

        model.addAttribute("newsById", newsRepository.getFullNewsById(id));
        model.addAttribute("currantNewsType", newsType);
        model.addAttribute("newsTypes",
                Arrays.stream(NewsType.values()).map(type -> type.toString().toLowerCase()));
        return "news/news-by-id";
    }

}
