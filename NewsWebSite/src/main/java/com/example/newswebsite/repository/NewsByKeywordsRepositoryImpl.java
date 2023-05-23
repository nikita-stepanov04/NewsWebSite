package com.example.newswebsite.repository;

import com.example.newswebsite.model.news.News;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

import java.util.*;


@RequiredArgsConstructor
public class NewsByKeywordsRepositoryImpl implements NewsByKeywordsRepository {

    private final EntityManager entityManager;

    @Override
    public Set<News> getNewsBySubstringAndThenByKeywords(String keywordsLine) {
        String[] keywords = keywordsLine.split("[^a-zA-Z0-9]+");
        List<String> keywordsList = List.of(keywords);
        Set<String> keywordsSet = new HashSet<>(Arrays.asList(keywords));

        if (keywords[0].equals("")) {
            return new HashSet<>();
        }

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<News> criteriaQuery = criteriaBuilder.createQuery(News.class);

        Root<News> root = criteriaQuery.from(News.class);
        Predicate combinedPredicate;

        // make a predicate to find news by substring

        String substring = '%' + String.join("%", keywordsList).toLowerCase() + '%';

        Predicate searchInTitle = criteriaBuilder
                .like(criteriaBuilder.lower(root.get("title")), substring);

        Predicate searchInBody = criteriaBuilder
                .like(criteriaBuilder.lower(root.get("fullBody")), substring);

        Predicate searchInType = criteriaBuilder
                .like(criteriaBuilder.lower(root.get("newsType")), substring);

        combinedPredicate = criteriaBuilder.or(
                searchInTitle,
                searchInBody,
                searchInType
        );

        // execute query with generated predicate to find by substring

        criteriaQuery.where(combinedPredicate);
        TypedQuery<News> typedQuery = entityManager.createQuery(criteriaQuery);
        Set<News> newsBySubstring = new LinkedHashSet<>(typedQuery.getResultList());

        // make a predicate to find news by keywords

        for (String keyword: keywordsSet) {

            Predicate searchInTitleByKeywords = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("title")),
                            '%' + keyword.toLowerCase()+ '%');

            Predicate searchInBodyByKeywords = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("fullBody")),
                            '%' + keyword.toLowerCase()+ '%');

            Predicate searchInTypeByKeywords = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("newsType")),
                            '%' + keyword.toLowerCase()+ '%');

            if (combinedPredicate != null) {
                combinedPredicate = criteriaBuilder.or(
                        combinedPredicate,
                        searchInTitleByKeywords,
                        searchInBodyByKeywords,
                        searchInTypeByKeywords
                );
            } else {
                combinedPredicate = criteriaBuilder.or(
                        searchInTitleByKeywords,
                        searchInBodyByKeywords,
                        searchInTypeByKeywords
                );
            }
        }

        // execute query with generated predicate to find news by keywords

        criteriaQuery.where(combinedPredicate);
        typedQuery = entityManager.createQuery(criteriaQuery);
        Set<News> newsByKeywords = new LinkedHashSet<>(typedQuery.getResultList());

        // merge two sets of news
        newsBySubstring.addAll(newsByKeywords);
        return newsBySubstring;
    }
}
