package com.example.newswebsite.controllers;

import com.example.newswebsite.model.history.History;
import com.example.newswebsite.model.news.NewsType;
import com.example.newswebsite.model.user.Role;
import com.example.newswebsite.model.user.User;
import com.example.newswebsite.repository.HistoryRepository;
import com.example.newswebsite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/myAccount")
public class UserAccountController {

    private User userData;
    private final UserRepository userRepository;
    private final HistoryRepository historyRepository;

    @Autowired
    public UserAccountController(UserRepository userRepository, HistoryRepository historyRepository) {
        this.userRepository = userRepository;
        this.historyRepository = historyRepository;
    }

    // if user role is admin this page will be shown after clicking on the News preview in the
    // admin console, we need to render admin console header on the top of the page, so we add
    // user role to model in order to recognize do we need to render admin console header (if user
    // is not an admin)
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

        List<History> history = historyRepository.findHistoriesByUserOrderByViewedAtDesc(userData);

        // grouping the array of histories by viewing days
        // get set of days
        Set<String> days = history
                .stream()
                // substring is used in order to ignore time
                .map(h -> h.getViewedAt().substring(0, 10))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        // get map with days as a key and list of news viewed that day as a value
        Map<String, List<History>> groupedByDayHistory = new LinkedHashMap<>();

        for (String day: days) {
            groupedByDayHistory.put(day, filterByDay(history, day));
        }

        model.addAttribute("currantOption", "history");
        model.addAttribute("groupedUserHistory", groupedByDayHistory);
        return "myAccount/my-account-history";
    }

    @DeleteMapping("/history/delete/{id}")
    public String processDelete(@PathVariable("id") Long id) {

        historyRepository.deleteById(id);
        return "redirect:/myAccount/history";
    }

    @Transactional
    @DeleteMapping("/history/clearAll")
    public String processDelete() {

        getUserData();
        historyRepository.deleteAllByUser(userData);
        return "redirect:/myAccount/history";
    }

    private void getUserData() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        userData = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User was not found"));
    }

    private List<History> filterByDay(List<History> histories, String day) {
        return histories
                .stream()
                // substring is used in order to ignore time
                .filter(h -> h.getViewedAt().substring(0, 10).equals(day))
                .toList();
    }
}
