package com.example.newswebsite.initialization;

import com.example.newswebsite.model.news.News;
import com.example.newswebsite.repository.NewsRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Component
public class NewsInitializer implements CommandLineRunner {

    private final NewsRepository newsRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public NewsInitializer(NewsRepository newsRepository,
                           ObjectMapper objectMapper) {
        this.newsRepository = newsRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) throws IOException {
        if (!newsRepository.findAll().iterator().hasNext()) {
            Resource resource = new ClassPathResource("/json/news.json");
            File newsJsonFile = resource.getFile();

            NewsContainer container = objectMapper.readValue(newsJsonFile,
                    new TypeReference<>(){});

            List<News> news = new LinkedList<>();

            container.getNewsList()
                    .forEach(item -> news.add(new News(
                            item.getNewsType(),
                            item.getTitle(),
                            item.getFullBody())));

            newsRepository.saveAll(news);
        }
    }
}
