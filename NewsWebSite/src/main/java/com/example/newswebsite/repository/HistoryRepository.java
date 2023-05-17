package com.example.newswebsite.repository;

import com.example.newswebsite.model.history.History;
import com.example.newswebsite.model.user.User;
import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HistoryRepository extends CrudRepository<History, Long> {
    List<History> findHistoriesByUserOrderByViewedAtDesc(User user);
    void deleteById(@NonNull Long id);
    void deleteAllByUser(User user);
}
