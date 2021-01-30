package ru.itmo.wp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itmo.wp.domain.Comment;
import ru.itmo.wp.domain.Post;
import ru.itmo.wp.security.Guest;
import ru.itmo.wp.service.PostService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class PostPage extends Page {
    private final PostService postService;

    public PostPage(PostService postService) {
        this.postService = postService;
    }

    @Guest
    @GetMapping("/post/{id}")
    public String postGet(Model model, @PathVariable String id) {
        model.addAttribute("comment", new Comment());
        model.addAttribute("post", postService.findById(parseLong(id)));
        return "PostPage";
    }

    @PostMapping("/post/{id}")
    public String writeCommentPost(@Valid @ModelAttribute("comment") Comment comment,
                                BindingResult bindingResult,
                                Model model,
                                HttpSession httpSession,
                                @PathVariable String id) {

        Post post = postService.findById(parseLong(id));

        if (bindingResult.hasErrors()) {
            model.addAttribute("post", post);
            return "PostPage";
        }

        comment.setUser(getUser(httpSession));
        postService.writeComment(post, comment);

        model.addAttribute("comment", new Comment());
        model.addAttribute("post", post);
        putMessage(httpSession, "You created new comment");

        return "PostPage";
    }
}
