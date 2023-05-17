package com.example.newswebsite.controllers;

import com.example.newswebsite.model.history.History;
import com.example.newswebsite.model.news.News;
import com.example.newswebsite.model.news.NewsType;
import com.example.newswebsite.model.user.User;
import com.example.newswebsite.repository.HistoryRepository;
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

import java.lang.reflect.Method;

import java.util.Arrays;

@Controller
@RequestMapping("/news")
public class NewsController {

    private final UserRepository userRepository;
    private final NewsRepository newsRepository;
    private final HistoryRepository historyRepository;

    @Autowired
    public NewsController(UserRepository userRepository,
                          NewsRepository newsRepository,
                          HistoryRepository historyRepository) {
        this.userRepository = userRepository;
        this.newsRepository = newsRepository;
        this.historyRepository = historyRepository;
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

        try {
            NewsType.valueOf(newsType.toUpperCase());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return "redirect:/news";
        }

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

        User user = userRepository.findByUsername(SecurityContextHolder
                        .getContext().getAuthentication().getName())
                .orElseThrow(() -> new UsernameNotFoundException("User was not found"));

        try {
            News news = newsRepository.findById(id)
                    .orElseThrow(() -> new Exception("News with given id was not found"));
            // update viewed news counter for currant news type
            // we can only obtain the string representation of the setter and getter methods
            // for each viewed news counter, so we use Java reflection to access them

            try {
                Method counterGetter = user.getClass().getDeclaredMethod("get" +
                        newsType.substring(0, 1).toUpperCase() +
                        newsType.substring(1) + "NewsViewedCounter");
                int newsCounter = (Integer) counterGetter.invoke(user);

                try {
                    Method counterSetter = user.getClass().getDeclaredMethod("set" +
                                    newsType.substring(0, 1).toUpperCase() +
                                    newsType.substring(1) + "NewsViewedCounter",
                            int.class);
                    counterSetter.invoke(user, newsCounter + 1);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // save operation to the history table, user with updated counter saves with history
            historyRepository.save(new History(user, news));

            model.addAttribute("newsById", news);
            model.addAttribute("currantNewsType", newsType);
            model.addAttribute("newsTypes",
                    Arrays.stream(NewsType.values()).map(type -> type.toString().toLowerCase()));
            return "news/news-by-id";

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/news";
        }
    }
}
