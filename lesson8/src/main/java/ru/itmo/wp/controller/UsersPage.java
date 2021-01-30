package ru.itmo.wp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.itmo.wp.domain.User;
import ru.itmo.wp.service.UserService;

import javax.servlet.http.HttpSession;

@Controller
public class UsersPage extends Page {
    private final UserService userService;

    public UsersPage(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/all")
    public String usersGet(Model model) {
        model.addAttribute("users", userService.findAll());
        return "UsersPage";
    }

    private String changeDisable(HttpSession httpSession, String id, boolean val) {
        User sessionUser = getUser(httpSession);
        if (sessionUser == null) {
            putMessage(httpSession, "User must be logged");
            return "redirect:/";
        }

        Long userId = parseLong(id);
        if (userId != null) {
            userService.setDisable(userId, val);
            if (sessionUser.getId() == userId) {
                putMessage(httpSession, "You disabled your account");
                unsetUser(httpSession);
                return "redirect:/";
            }
        }

        putMessage(httpSession, "You disabled the account");
        return "redirect:/users/all";
    }

    @GetMapping("users/enable{id:-?[1-9][0-9]{0,18}}")
    public String userEnable(@PathVariable String id, HttpSession httpSession) {
        return changeDisable(httpSession, id, false);
    }

    @GetMapping("users/disable{id:-?[1-9][0-9]{0,18}}")
    public String userDisable(@PathVariable String id, HttpSession httpSession) {
        return changeDisable(httpSession, id, true);
    }
}
