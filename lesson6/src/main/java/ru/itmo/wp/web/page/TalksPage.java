package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.Talk;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unused", "RedundantSuppression"})
public class TalksPage extends Page {

    private List<Talk.TalkWithNames> findAllTalksWithNames(long userId) {
        List<Talk> talks = talkService.findAllTalks(userId);
        List<Talk.TalkWithNames> talkWithNames = new ArrayList<>();
        for (Talk talk : talks) {
            Talk.TalkWithNames newTalk = new Talk.TalkWithNames(talk);
            newTalk.setSourceUserName(userService.findById(talk.getSourceUserId()).getLogin());
            newTalk.setTargetUserName(userService.findById(talk.getTargetUserId()).getLogin());
            talkWithNames.add(newTalk);
        }
        return talkWithNames;
    }

    @Override
    protected void before(HttpServletRequest request, Map<String, Object> view) {
        super.before(request, view);
        User user = getUser();
        if (user == null) {
            setMessage("User must be authorized to talk");
            throw new RedirectException("/index");
        }
    }

    @Override
    protected void after(HttpServletRequest request, Map<String, Object> view) {
        super.after(request, view);
        User user = getUser();
        view.put("users", userService.findAllUsers());
        view.put("talks", findAllTalksWithNames(user.getId()));
    }

    protected void sendMessage(HttpServletRequest request, Map<String, Object> view) throws ValidationException {
        Talk talk = new Talk();

        User user = getUser();
        userService.validateUserId(request.getParameter("selectedUser"));
        talk.setSourceUserId(user.getId());
        talk.setTargetUserId(userService.findUserByLogin(request.getParameter("selectedUser")).getId());
        talk.setText(request.getParameter("text"));

        talkService.validationTalk(talk);
        talkService.saveTalk(talk);
        setMessage("Message created!");
        throw new RedirectException("/talks");
    }
}
