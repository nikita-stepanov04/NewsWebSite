package com.example.newswebsite.controllers;

import com.example.newswebsite.model.news.News;
import com.example.newswebsite.model.news.NewsType;
import com.example.newswebsite.model.news.NewsUpdateFormResponse;
import com.example.newswebsite.repository.NewsRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;

@Controller
@RequestMapping("/adminConsole")
public class AdminConsoleController {

    NewsRepository newsRepository;
    NewsUpdateFormResponse newsUpdateFormResponse;

    @Autowired
    public AdminConsoleController(NewsRepository newsRepository,
                                  NewsUpdateFormResponse newsUpdateFormResponse) {
        this.newsRepository = newsRepository;
        this.newsUpdateFormResponse = newsUpdateFormResponse;
    }

    @GetMapping
    public String adminConsole() {
        return "redirect:/adminConsole/updateNews";
    }

    @GetMapping("/updateNews")
    public String updateNews(Model model) {
        model.addAttribute("currantOption", "updateNews");
        model.addAttribute("newsUpdateFormResponse", newsUpdateFormResponse);
        model.addAttribute("newsTypes",
                Arrays.stream(NewsType.values()).map(type -> type.toString().toLowerCase()));
        return "adminConsole/update-news";
    }

    @PostMapping("/updateNews")
    public String processNews(@ModelAttribute @Valid NewsUpdateFormResponse newsUpdateFormResponse,
                              Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("newsTypes",
                    Arrays.stream(NewsType.values()).map(type -> type.toString().toLowerCase()));
            return "adminConsole/update-news";
        }

        News news = new News(newsUpdateFormResponse.getType(),
                newsUpdateFormResponse.getTitle(),
                newsUpdateFormResponse.getBody());

        newsRepository.save(news);
        return "redirect:/updateNews";
    }

    @GetMapping("/editNews")
    public String editNews(Model model) {
        model.addAttribute("currantOption", "editNews");
        return "adminConsole/update-news";
    }

    @GetMapping("/newsPreview")
    public String newsPreview(Model model) {
        model.addAttribute("currantOption", "newsPreview");
        return "adminConsole/update-news";
    }
}
