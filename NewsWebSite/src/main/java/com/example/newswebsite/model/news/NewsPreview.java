package com.example.newswebsite.model.news;

public interface NewsPreview {
    Long getId();
    String getNewsType();
    String getTitle();
    String getShortBody();
    String getCreatedAt();
}
