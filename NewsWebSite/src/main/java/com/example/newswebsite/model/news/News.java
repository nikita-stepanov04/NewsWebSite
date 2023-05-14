package com.example.newswebsite.model.news;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "news")
public class News {

    @Id
    @GeneratedValue
    private Long id;

    private String newsType;

    private String title;

    @Column(length = 3000)
    private String fullBody;

    @Column(length = 500)
    private String shortBody;

    private String createdAt;

    public static class NewsBuilder {
        public News build() {
            String shortBody = Stream.of(fullBody.split(" "))
                    .limit(50)
                    .collect(Collectors.joining(" "));
            shortBody += " ...";

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

            return new News(id, newsType, title, fullBody, shortBody, now.format(formatter));
        }
    }
}
