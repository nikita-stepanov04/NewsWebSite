package com.example.newswebsite.model.news;

import java.util.Date;

public interface FullNews {
    Long getId();
    String getNewsType();
    String getTitle();
    String getFullBody();
    Date getCreatedAt();
}
