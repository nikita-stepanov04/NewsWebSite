package com.example.newswebsite.model.news;

import java.util.Date;

public interface NewsPreview {
    Long getId();
    String getNewsType();
    String getTitle();
    String getShortBody();
    String getCreatedAt();
}
