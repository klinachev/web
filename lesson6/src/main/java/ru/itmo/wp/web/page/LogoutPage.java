package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@SuppressWarnings({"unused", "RedundantSuppression"})
public class LogoutPage extends Page {
    protected void action(HttpServletRequest request, Map<String, Object> view) {
        User user = getUser();
        if (user != null) {
            eventService.logout(user);
        }

        request.getSession().removeAttribute("user");

        setMessage("Good bye. Hope to see you soon!");
        throw new RedirectException("/index");
    }
}
