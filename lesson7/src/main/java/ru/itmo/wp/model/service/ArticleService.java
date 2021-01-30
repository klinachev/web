package ru.itmo.wp.model.service;

import com.google.common.base.Strings;
import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.repository.ArticleRepository;
import ru.itmo.wp.model.repository.impl.ArticleRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

public class ArticleService {
    ArticleRepository articleRepository = new ArticleRepositoryImpl();

    public void validationArticle(Article article) throws ValidationException {
        if (Strings.isNullOrEmpty(article.getTitle())) {
            throw new ValidationException("Title is required");
        }
        if (article.getTitle().length() > 16) {
            throw new ValidationException("Title can't be longer than 16 letters");
        }
        if (Strings.isNullOrEmpty(article.getText())) {
            throw new ValidationException("Text is required");
        }
        if (article.getText().length() > 250) {
            throw new ValidationException("Text can't be longer than 250 letters");
        }
    }

    public void createArticle(Article article) {
        articleRepository.save(article);
    }

    public Article find(long id) {
        return articleRepository.findById(id);
    }

    public List<Article> findAllUnhiddenArticles() {
        return articleRepository.findAllUnhiddenArticles();
    }

    public List<Article> findAllByUserId(long id) {
        return articleRepository.findAllByUserId(id);
    }

    public void changeHidden(Article article, boolean newHidden) {
        articleRepository.setHidden(article.getId(), newHidden);
        article.setHidden(newHidden);
    }
}
