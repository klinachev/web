package ru.itmo.wp.model.repository;

import ru.itmo.wp.model.domain.Event;

public interface EventRepository {
    boolean isExist(String name);
    Event findById(long id);
    void save(Event event);
}
