package ru.itmo.wp.model.repository;

import ru.itmo.wp.model.domain.Article;

import java.util.List;

public interface ArticleRepository {
    Article findById(long id);
    List<Article> findAllByUserId(long id);
    List<Article> findAllUnhiddenArticles();
    void setHidden(long id, boolean val);
    void save(Article article);
}
