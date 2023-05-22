package com.example.newswebsite.repository;

import com.example.newswebsite.model.news.News;
import com.example.newswebsite.model.news.NewsPreview;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.List;

public interface NewsRepository extends CrudRepository<News, Long>, NewsByKeywordsRepository {
    List<NewsPreview> getNewsPreviewsByNewsType(String newsType);
    void deleteById(@NonNull Long id);
}
