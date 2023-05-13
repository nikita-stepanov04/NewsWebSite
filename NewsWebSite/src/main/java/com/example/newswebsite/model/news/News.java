package com.example.newswebsite.model.news;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
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
    private String body;

    private Date createdAt;

    public NewsPreview toPreview() {
        String shortBody = Stream.of(body.split(" "))
                .limit(50)
                .collect(Collectors.joining(" "));
        return new NewsPreview(id, title, shortBody + " ...");
    }
}
