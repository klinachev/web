package ru.itmo.web.lesson4.model;


public class User {
    public enum UserColor {
        RED, GREEN, BLUE
    }

    private final long id;
    private final String handle;
    private final String name;
    private final UserColor color;


    public User(long id, String handle, String name, UserColor color) {
        this.id = id;
        this.handle = handle;
        this.name = name;
        this.color = color;
    }

    public long getId() {
        return id;
    }

    public String getHandle() {
        return handle;
    }

    public String getName() {
        return name;
    }

    public UserColor getColor() {
        return color;
    }
}
