package ru.itmo.wp.model.repository;

import ru.itmo.wp.model.domain.User;

import java.util.List;

public interface UserRepository {
    User findById(long id);
    User findByLogin(String login);
    User findByEmail(String email);
    User findByLoginOrEmailAndPasswordSha(String loginOrEmail, String passwordSha);
    List<User> findAll();
    long findCount();
    void setAdmin(long id, boolean val);
    void save(User user, String passwordSha);
}
