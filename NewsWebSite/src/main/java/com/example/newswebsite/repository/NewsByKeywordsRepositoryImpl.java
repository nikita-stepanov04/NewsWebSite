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
    public Set<News> getNewsByKeywords(String keywordString) {

        String[] keywords = keywordString.split("[^a-zA-Z0-9]+");
        Set<String> keywordsSet = new HashSet<>(Arrays.asList(keywords));

        if (keywordsSet.isEmpty()) {
            return null;
        }

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<News> criteriaQuery = criteriaBuilder.createQuery(News.class);

        Root<News> root = criteriaQuery.from(News.class);
        Predicate combinedPredicate = null;

        for (String keyword: keywords) {

            Predicate searchInTitle = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("title")),
                            '%' + keyword.toLowerCase()+ '%');

            Predicate searchInBody = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("fullBody")),
                            '%' + keyword.toLowerCase()+ '%');

            Predicate searchInType = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("newsType")),
                            '%' + keyword.toLowerCase()+ '%');

            if (combinedPredicate != null) {
                combinedPredicate = criteriaBuilder.or(
                        combinedPredicate,
                        searchInTitle,
                        searchInBody,
                        searchInType
                );
            } else {
                combinedPredicate = criteriaBuilder.or(
                        searchInTitle,
                        searchInBody,
                        searchInType
                );
            }
        }

        criteriaQuery.where(combinedPredicate);
        TypedQuery<News> typedQuery = entityManager.createQuery(criteriaQuery);
        return new LinkedHashSet<>(typedQuery.getResultList());
    }


    @Override
    public Set<News> getNewsBySubstring(String substring) {
        String[] keywords = substring.split("[^a-zA-Z0-9]+");
        List<String> keywordsList = List.of(keywords);

        if (keywordsList.isEmpty()) {
            return null;
        }

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<News> criteriaQuery = criteriaBuilder.createQuery(News.class);

        Root<News> root = criteriaQuery.from(News.class);

        substring = '%' + String.join("%", keywordsList).toLowerCase() + '%';
        System.out.println("\nSUBSTRING IS: \n" + substring);


        Predicate searchInTitle = criteriaBuilder
                .like(criteriaBuilder.lower(root.get("title")), substring);

        Predicate searchInBody = criteriaBuilder
                .like(criteriaBuilder.lower(root.get("fullBody")), substring);

        Predicate searchInType = criteriaBuilder
                .like(criteriaBuilder.lower(root.get("newsType")), substring);

        Predicate combinedPredicate = criteriaBuilder.or(
                searchInTitle,
                searchInBody,
                searchInType
        );

        criteriaQuery.where(combinedPredicate);
        TypedQuery<News> typedQuery = entityManager.createQuery(criteriaQuery);
        return new LinkedHashSet<>(typedQuery.getResultList());
    }
}
