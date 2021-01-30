package ru.itmo.web.lesson4.model;

public class Post {
    final private long id;
    final private long userId;
    final private String title;
    final private String text;

    public Post(long id, long userId, String title, String text) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}
