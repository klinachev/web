package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.service.ArticleService;
import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/** @noinspection unused*/
public class MyArticlesPage {
    protected ArticleService articleService = new ArticleService();

    private User validateUser(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            request.getSession().setAttribute("message", "Only authorized users have articles");
            throw new RedirectException("/index");
        }
        return user;
    }

    private void action(HttpServletRequest request, Map<String, Object> view) {
        validateUser(request);
        findAllArticlesById(request, view);
    }

    private void findAllArticlesById(HttpServletRequest request, Map<String, Object> view) {
        User user = validateUser(request);
        view.put("articles", articleService.findAllByUserId(user.getId()));
    }

    private void changeArticleHidden(HttpServletRequest request, Map<String, Object> view) {
        User user = validateUser(request);
        long id = Long.parseLong(request.getParameter("id"));
        boolean newHidden = Boolean.parseBoolean(request.getParameter("newHidden"));
        Article article = articleService.find(id);
        if (article.getUserId() == user.getId()) {
            articleService.changeHidden(article, newHidden);
        }
        view.put("article", article);
    }
}
