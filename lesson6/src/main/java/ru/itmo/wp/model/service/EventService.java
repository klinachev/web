package ru.itmo.wp.model.service;

import ru.itmo.wp.model.domain.Event;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.repository.EventRepository;
import ru.itmo.wp.model.repository.impl.EventRepositoryImpl;

public class EventService {
    private final EventRepository eventRepository = new EventRepositoryImpl();

    private void createEvent(User user, Event.Type type) {
        Event event = new Event();
        event.setUserId(user.getId());
        event.setType(type);
        eventRepository.save(event);
    }

    public void enter(User user) {
        createEvent(user, Event.Type.ENTER);
    }

    public void logout(User user) {
        createEvent(user, Event.Type.LOGOUT);
    }
}
