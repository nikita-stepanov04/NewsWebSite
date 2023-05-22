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
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;

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
            model.addAttribute("currantOption", "updateNews");
            return "adminConsole/update-news";
        }

        News news = new News(newsUpdateFormResponse.getType(),
                newsUpdateFormResponse.getTitle(),
                newsUpdateFormResponse.getBody());

        newsRepository.save(news);
        return "redirect:/adminConsole/updateNews";
    }

    @GetMapping("/editNews")
    public String editNews() {
        return "redirect:/adminConsole/editNews/politics";
    }

    @GetMapping("/editNews/{newsType}")
    public String editNewsByType(@PathVariable("newsType") String newsType,
                                 Model model) {

        try {
            NewsType.valueOf(newsType.toUpperCase());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return "redirect:/adminConsole/editNews";
        }

        model.addAttribute("currantOption", "editNews");
        model.addAttribute("currantNewsType", newsType);
        model.addAttribute("newsTypes",
                Arrays.stream(NewsType.values()).map(type -> type.toString().toLowerCase()));
        model.addAttribute("newsPreviews", newsRepository.getNewsPreviewsByNewsType(newsType));

        return "adminConsole/edit-news";
    }

    @GetMapping("/editNewsById/{id}")
    public String editNewsById(@PathVariable("id") Long id,
                               Model model) {
        Optional<News> news = newsRepository.findById(id);

        if (news.isEmpty()) {
            return "redirect:/adminConsole/editNews";
        }

        NewsUpdateFormResponse response = new NewsUpdateFormResponse(news.get().getNewsType(),
                news.get().getTitle(), news.get().getFullBody());

        model.addAttribute("currantOption", "updateNews");
        model.addAttribute("newsUpdateFormResponse", response);
        model.addAttribute("currantNewsType", news.get().getNewsType());
        model.addAttribute("newsTypes",
                Arrays.stream(NewsType.values()).map(type -> type.toString().toLowerCase()));
        return "adminConsole/edit-news-byId";
    }

    @PostMapping("/editNewsById/{id}")
    public String saveEditedNews(@PathVariable("id") Long id,
                                 @ModelAttribute @Valid NewsUpdateFormResponse newsUpdateFormResponse,
                                 Errors errors, Model model) {
        System.out.println(errors);
        if (errors.hasErrors()) {
            model.addAttribute("newsTypes",
                    Arrays.stream(NewsType.values()).map(type -> type.toString().toLowerCase()));
            model.addAttribute("currantOption", "updateNews");
            return "adminConsole/edit-news-byId";
        }

        newsRepository.deleteById(id);

        News news = new News(newsUpdateFormResponse.getType(),
                newsUpdateFormResponse.getTitle(),
                newsUpdateFormResponse.getBody());

        newsRepository.save(news);
        return "redirect:/adminConsole/editNews";

    }

    @GetMapping("/deleteNewsById/{id}")
    public String deleteNewsById(@PathVariable("id") Long id) {
        newsRepository.deleteById(id);
        return "redirect:/adminConsole/editNews";
    }

    @GetMapping("/newsPreview")
    public String newsPreview(Model model) {
        model.addAttribute("currantOption", "newsPreview");
        return "adminConsole/update-news";
    }
}
