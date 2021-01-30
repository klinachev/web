package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.service.ArticleService;
import ru.itmo.wp.model.service.UserService;
import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @noinspection unused
 */
public class UsersPage {
    private final UserService userService = new UserService();
    private final ArticleService articleService = new ArticleService();

    private void action(HttpServletRequest request, Map<String, Object> view) {
        User user = (User) request.getSession().getAttribute("user");
        if (user != null && user.isAdmin()) {
            view.put("user", user);
        }
    }

    private void findAll(HttpServletRequest request, Map<String, Object> view) {
        view.put("users", userService.findAll());
    }

    private void findUser(HttpServletRequest request, Map<String, Object> view) {
        view.put("user",
                userService.find(Long.parseLong(request.getParameter("userId"))));
    }

    private void changeAdminValue(HttpServletRequest request, Map<String, Object> view) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || !user.isAdmin()) {
            throw new RedirectException("/users");
        }
        long adminCandidateId = Long.parseLong(request.getParameter("adminCandidateId"));
        User adminCandidate = userService.find(adminCandidateId);
        boolean isAdmin = Boolean.parseBoolean(request.getParameter("isAdmin"));
        userService.changeAdminValue(adminCandidate, isAdmin);
        view.put("user", adminCandidate);
        if (adminCandidate.getId() == user.getId()) {
            request.getSession().setAttribute("user", adminCandidate);
        }
    }
}
