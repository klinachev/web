package ru.itmo.wp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itmo.wp.form.NoticeData;
import ru.itmo.wp.form.validator.NoticeDataCreateValidator;
import ru.itmo.wp.service.NoticeService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class CreateNoticePage extends Page {
    private final NoticeService noticeService;
    private final NoticeDataCreateValidator noticeDataCreateValidator;

    public CreateNoticePage(NoticeService noticeService, NoticeDataCreateValidator noticeDataCreateValidator) {
        this.noticeService = noticeService;
        this.noticeDataCreateValidator = noticeDataCreateValidator;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        if (binder == null || binder.getTarget() == null) return;
        if (noticeDataCreateValidator.supports(binder.getTarget().getClass()))
            binder.addValidators(noticeDataCreateValidator);
    }

    @GetMapping("/createNotice")
    public String createNoticeGet(Model model, HttpSession httpSession) {
        if (noUserInSessionCheck(httpSession)) {
            return "redirect:";
        }
        model.addAttribute("noticeForm", new NoticeData());
        return "CreateNoticePage";
    }

    @PostMapping("/createNotice")
    public String createNoticePost(@Valid @ModelAttribute("noticeForm") NoticeData noticeData,
                               BindingResult bindingResult,
                               HttpSession httpSession) {
        if (bindingResult.hasErrors()) {
            return "CreateNoticePage";
        }

        if (noUserInSessionCheck(httpSession)) {
            return "redirect:";
        }

        noticeService.saveNotice(noticeData);
        putMessage(httpSession, "Notice created");

        return "redirect:";
    }
}
