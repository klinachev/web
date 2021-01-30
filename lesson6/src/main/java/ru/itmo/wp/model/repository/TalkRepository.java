package ru.itmo.wp.model.repository;

import ru.itmo.wp.model.domain.Talk;

import java.util.List;

public interface TalkRepository {
    boolean isExist(String name);
    Talk findById(long id);
    List<Talk> findAllByUserId(long UserId);
    void save(Talk talk);
}
