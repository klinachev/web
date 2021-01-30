package ru.itmo.wp.model.service;

import com.google.common.base.Strings;
import ru.itmo.wp.model.domain.Talk;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.repository.TalkRepository;
import ru.itmo.wp.model.repository.impl.TalkRepositoryImpl;

import java.util.List;

public class TalkService {
    private final TalkRepository talkRepository = new TalkRepositoryImpl();

    public void validationTalk(Talk talk) throws ValidationException {
        if (Strings.isNullOrEmpty(talk.getText())) {
            throw new ValidationException("Text is required");
        }
        if (talk.getText().length() < 5) {
            throw new ValidationException("Text can't be shorter than 5 letters");
        }
        if (talk.getText().length() > 250) {
            throw new ValidationException("Text can't be longer than 250 letters");
        }
    }

    public void saveTalk(Talk talk) {
        talkRepository.save(talk);
    }

    public List<Talk> findAllTalks(long userId) {
        return talkRepository.findAllByUserId(userId);
    }
}
