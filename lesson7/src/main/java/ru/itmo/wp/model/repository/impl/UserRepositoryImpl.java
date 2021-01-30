package ru.itmo.wp.model.repository.impl;

import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.RepositoryException;
import ru.itmo.wp.model.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

public class UserRepositoryImpl extends BaseRepositoryImpl<User> implements UserRepository {

    public UserRepositoryImpl() {
        super("User");
    }

    @Override
    public void setAdmin(long id, boolean val) {
        setBoolean(id, val, "admin");
    }

    private User findBySth(String argName, String arg) {
        try (DbState dbState = new DbState("SELECT * FROM User WHERE " + argName + "=?")) {
            PreparedStatement statement = dbState.getStatement();
            statement.setString(1, arg);
            try (ResultSet resultSet = statement.executeQuery()) {
                return toUser(statement.getMetaData(), resultSet);
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find User.", e);
        }
    }

    @Override
    public User findByLogin(String login) {
        return findBySth("login", login);
    }

    @Override
    public User findByEmail(String email) {
        return findBySth("email", email);
    }

    public User findByLoginOrEmailAndPasswordSha(String loginOrEmail, String passwordSha) {
        try (DbState dbState = new DbState("SELECT * FROM User WHERE (login=? OR email=?) AND passwordSha=?")) {
            PreparedStatement statement = dbState.getStatement();
            statement.setString(1, loginOrEmail);
            statement.setString(2, loginOrEmail);
            statement.setString(3, passwordSha);
            try (ResultSet resultSet = statement.executeQuery()) {
                return toUser(statement.getMetaData(), resultSet);
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find User.", e);
        }
    }

    @Override
    public List<User> findAll() {
        return findAll("ORDER BY id DESC");
    }

    @Override
    public User toObject(ResultSetMetaData resultSetMetaData, ResultSet resultSet) throws SQLException {
        return toUser(resultSetMetaData, resultSet);
    }

    private User toUser(ResultSetMetaData metaData, ResultSet resultSet) throws SQLException {
        if (!resultSet.next()) {
            return null;
        }

        User user = new User();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            switch (metaData.getColumnName(i)) {
                case "id":
                    user.setId(resultSet.getLong(i));
                    break;
                case "login":
                    user.setLogin(resultSet.getString(i));
                    break;
                case "email":
                    user.setEmail(resultSet.getString(i));
                    break;
                case "creationTime":
                    user.setCreationTime(resultSet.getTimestamp(i));
                    break;
                case "admin":
                    user.setAdmin(resultSet.getBoolean(i));
                default:
                    // No operations.
            }
        }

        return user;
    }

    @Override
    public void save(User user, String passwordSha) {
        List<Object> parameters = List.of(user, passwordSha);
        save("(`login`, `email`, `passwordSha`, `creationTime`)", "(?, ?, ?, NOW())", parameters);
    }

    @Override
    protected void saveMethodSetParameters(PreparedStatement statement, List<Object> parameters) throws SQLException {
        User user = (User) parameters.get(0);
        String passwordSha = (String) parameters.get(1);
        statement.setString(1, user.getLogin());
        statement.setString(2, user.getEmail());
        statement.setString(3, passwordSha);
    }

    @Override
    protected void saveMethodSetGeneratedKeys(ResultSet generatedKeys, List<Object> parameters) throws SQLException {
        User user = (User) parameters.get(0);
        user.setId(generatedKeys.getLong(1));
        user.setCreationTime(findById(user.getId()).getCreationTime());
    }
}
