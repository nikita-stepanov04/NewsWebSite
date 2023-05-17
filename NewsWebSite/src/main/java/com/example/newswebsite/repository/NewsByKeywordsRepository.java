package com.example.newswebsite.repository;

import com.example.newswebsite.model.news.News;
import java.util.Set;

public interface NewsByKeywordsRepository {
    Set<News> getNewsByKeywords(String keywordSet);
    Set<News> getNewsBySubstring(String substring);
}
