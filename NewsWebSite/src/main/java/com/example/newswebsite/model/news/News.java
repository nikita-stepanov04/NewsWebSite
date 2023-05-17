package com.example.newswebsite.model.news;

import com.example.newswebsite.model.history.History;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Data
@Table(name = "news")
@NoArgsConstructor
public class News {
    public News(String newsType, String title, String fullBody) {
        this.newsType = newsType;
        this.title = title;
        this.fullBody = fullBody;

        String shortBody = Stream.of(fullBody.split(" "))
                .limit(50)
                .collect(Collectors.joining(" "));
        shortBody += " ...";

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        this.shortBody = shortBody;
        this.createdAt = formatter.format(now);
    }

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

    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL)
    private List<History> histories = new LinkedList<>();
}
