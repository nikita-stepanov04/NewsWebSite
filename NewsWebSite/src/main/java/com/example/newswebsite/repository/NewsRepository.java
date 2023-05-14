package com.example.newswebsite.repository;

import com.example.newswebsite.model.news.FullNews;
import com.example.newswebsite.model.news.News;
import com.example.newswebsite.model.news.NewsPreview;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NewsRepository extends CrudRepository<News, Long> {
    List<NewsPreview> getNewsPreviewsByNewsType(String newsType);
    FullNews getFullNewsById(Long id);
}
