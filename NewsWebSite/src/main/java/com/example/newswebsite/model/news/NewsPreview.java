package com.example.newswebsite.model.news;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewsPreview {
    private Long id;
    private String title;
    private String shortBody;
}
