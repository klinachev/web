package ru.itmo.wp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.itmo.wp.service.UserService;

@Controller
public class UserPage extends Page {
    UserService userService;

    public UserPage(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("user/{id}")
    public String userGet(Model model, @PathVariable String id) {
        model.addAttribute("user", userService.findById(parseLong(id)));
        return "UserPage";
    }
}
