package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.service.ArticleService;
import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/** @noinspection unused*/
public class ArticlePage {
    protected final ArticleService articleService = new ArticleService();

    private User validateUser(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            request.getSession().setAttribute("message", "Only authorized users can make article");
            throw new RedirectException("/index");
        }
        return user;
    }

    private void action(HttpServletRequest request, Map<String, Object> view) {
        validateUser(request);
    }

    private void createArticle(HttpServletRequest request, Map<String, Object> view) throws ValidationException {
        User user = validateUser(request);

        String title = request.getParameter("title");
        String text = request.getParameter("text");

        Article article = new Article();
        article.setTitle(title);
        article.setText(text);
        article.setUserId(user.getId());

        articleService.validationArticle(article);
        articleService.createArticle(article);

        request.getSession().setAttribute("message", "Article created");
        throw new RedirectException("/index");
    }
}
