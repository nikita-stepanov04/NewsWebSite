package com.example.newswebsite.repository;

import com.example.newswebsite.model.news.News;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NewsRepository extends CrudRepository<News, Long> {
    List<News> getNewsByNewsType(String newsType);
}
