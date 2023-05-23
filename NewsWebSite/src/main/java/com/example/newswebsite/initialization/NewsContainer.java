package com.example.newswebsite.initialization;

import com.example.newswebsite.model.news.News;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class NewsContainer {
    @JsonProperty("news")
    private List<News> newsList;

    public List<News> getNewsList() {
        return newsList;
    }

}
