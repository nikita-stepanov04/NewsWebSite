package com.example.newswebsite.repository;

import com.example.newswebsite.model.news.News;
import java.util.Set;

public interface NewsByKeywordsRepository {
    Set<News> getNewsBySubstringAndThenByKeywords(String keywordsLine);
}
