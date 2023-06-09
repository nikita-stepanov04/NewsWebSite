package com.example.newswebsite.controllers;

import com.example.newswebsite.model.history.History;
import com.example.newswebsite.model.news.News;
import com.example.newswebsite.model.news.NewsPreview;
import com.example.newswebsite.model.news.NewsType;
import com.example.newswebsite.model.user.Role;
import com.example.newswebsite.model.user.User;
import com.example.newswebsite.repository.HistoryRepository;
import com.example.newswebsite.repository.NewsRepository;
import com.example.newswebsite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;

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

    // if user role is admin this page will be shown after clicking on the News preview in the
    // admin console, we need to render admin console header on the top of the page, so we add
    // user role to model in order to recognize do we need to render admin console header or not
    // (if user is not an admin)

    @ModelAttribute("role")
    public String addRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority(Role.ADMIN.toString()))) {
            return Role.ADMIN.toString();
        }
        return Role.USER.toString();
    }

    // and adminConsoleCurrantOption to underline the currant option in the header
    // if user role is user it would be ignored

    @ModelAttribute("adminConsoleCurrantOption")
    public String addAdminConsoleCurrantOption() {
        return "newsPreview";
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
        if (newsType.equals("allNews")) {
            model.addAttribute("currantNewsType", "allNews");
            model.addAttribute("newsPreviews",
                    newsRepository.getAllPreviews()
                            .stream()
                            .sorted(Comparator.comparing(NewsPreview::getCreatedAt).reversed())
                            .toList()
            );
        } else {
            try {
                NewsType.valueOf(newsType.toUpperCase());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return "redirect:/news";
            }

            model.addAttribute("currantNewsType", newsType);
            model.addAttribute("newsPreviews",
                    newsRepository.getNewsPreviewsByNewsType(newsType)
                            .stream()
                            .sorted(Comparator.comparing(NewsPreview::getCreatedAt).reversed())
                            .toList()
            );
        }

        model.addAttribute("newsTypes",
                Arrays.stream(NewsType.values()).map(type -> type.toString().toLowerCase()));

        return "news/news";
    }

    @GetMapping("/byKeywords")
    public String findNewsByKeywords(@RequestParam("keywords") String keywordString,
                                     Model model) {
        Set<News> news = newsRepository.getNewsBySubstringAndThenByKeywords(keywordString);

        model.addAttribute("currantNewsType", null);
        model.addAttribute("newsTypes",
                Arrays.stream(NewsType.values()).map(type -> type.toString().toLowerCase()));
        model.addAttribute("newsPreviews", news);

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
            // we can only obtain the string representation of the name of setter and getter methods
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
