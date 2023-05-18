package com.example.newswebsite.model.news;

import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class NewsUpdateFormResponse {
    private String type;

    @Size(min = 5, message = "Minimal title length is 5 characters")
    @Size(max = 255, message = "Too long title, max 255 characters")
    private String title;

    @Size(min = 5, message = "Minimal body length is 50 characters")
    @Size(max = 3000, message = "Too long body, max 3000 characters")
    private String body;
}
