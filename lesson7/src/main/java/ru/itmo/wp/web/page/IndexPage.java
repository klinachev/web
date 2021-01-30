package ru.itmo.wp.web.page;

import com.google.common.base.Strings;
import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.service.ArticleService;
import ru.itmo.wp.model.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** @noinspection unused*/
public class IndexPage {
    protected final ArticleService articleService = new ArticleService();
    protected final UserService userService = new UserService();

    private void action(HttpServletRequest request, Map<String, Object> view) {
        putMessage(request, view);
    }

    private void putMessage(HttpServletRequest request, Map<String, Object> view) {
        String message = (String) request.getSession().getAttribute("message");
        if (!Strings.isNullOrEmpty(message)) {
            view.put("message", message);
            request.getSession().removeAttribute("message");
        }
    }

    public void findUser(HttpServletRequest request, Map<String, Object> view) {
        view.put("user", userService.find(Long.parseLong(request.getParameter("userId"))));
    }

    public void findAllUnhiddenArticles(HttpServletRequest request, Map<String, Object> view) {
        List<Article> articles = articleService.findAllUnhiddenArticles();
        List<Article.ArticleWithName> articleWithNames = new ArrayList<>();
        for (Article article: articles) {
            String userName = userService.find(article.getUserId()).getLogin();
            Article.ArticleWithName articleWithName = new Article.ArticleWithName(article, userName);
            articleWithNames.add(articleWithName);
        }
        view.put("articles", articleWithNames);
    }
}
