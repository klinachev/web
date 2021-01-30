package ru.itmo.wp.model.repository.impl;

import ru.itmo.wp.model.domain.Talk;
import ru.itmo.wp.model.exception.RepositoryException;
import ru.itmo.wp.model.repository.TalkRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class TalkRepositoryImpl extends BaseRepositoryImpl<Talk> implements TalkRepository {
    public TalkRepositoryImpl() {
        super("Talk");
    }

    @Override
    public List<Talk> findAllByUserId(long userId) {
        List<Talk> objects = new ArrayList<>();
        try (DbState dbState = new DbState("SELECT * FROM Talk WHERE (sourceUserId=? OR targetUserId=?) ORDER BY creationTime DESC")) {
            PreparedStatement statement = dbState.getStatement();
            statement.setLong(1, userId);
            statement.setLong(2, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                Talk talk;
                while ((talk = toObject(statement.getMetaData(), resultSet)) != null) {
                    objects.add(talk);
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find Object.", e);
        }
        return objects;
    }

    @Override
    public Talk toObject(ResultSetMetaData metaData, ResultSet resultSet) throws SQLException {
        return toTalk(metaData, resultSet);
    }

    private Talk toTalk(ResultSetMetaData metaData, ResultSet resultSet) throws SQLException {
        if (!resultSet.next()) {
            return null;
        }

        Talk talk = new Talk();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            switch (metaData.getColumnName(i)) {
                case "id":
                    talk.setId(resultSet.getLong(i));
                    break;
                case "sourceUserId":
                    talk.setSourceUserId(resultSet.getLong(i));
                    break;
                case "targetUserId":
                    talk.setTargetUserId(resultSet.getLong(i));
                    break;
                case "text":
                    talk.setText(resultSet.getString(i));
                    break;
                case "creationTime":
                    talk.setCreationTime(resultSet.getTimestamp(i));
                    break;
                default:
                    // No operations.
            }
        }

        return talk;
    }

    @FunctionalInterface
    public interface MyRunnable {
        void run(PreparedStatement statement) throws SQLException;
    }

    @Override
    public void save(Talk talk) {
        List<Object> parameters = List.of(talk);
        MyRunnable a = (PreparedStatement statement) -> {
            statement.setLong(1, talk.getSourceUserId());
            statement.setLong(2, talk.getTargetUserId());
            statement.setString(3, talk.getText());
        };
        save("(`sourceUserId`, `targetUserId`, `text`, `creationTime`)", "(?, ?, ?, NOW())", parameters);
    }

    @Override
    protected void saveMethodSetParameters(PreparedStatement statement, List<Object> parameters) throws SQLException {
        Talk talk = (Talk) parameters.get(0);
        statement.setLong(1, talk.getSourceUserId());
        statement.setLong(2, talk.getTargetUserId());
        statement.setString(3, talk.getText());
    }

    @Override
    protected void saveMethodSetGeneratedKeys(ResultSet generatedKeys, List<Object> parameters) throws SQLException {
        Talk talk = (Talk) parameters.get(0);
        talk.setId(generatedKeys.getLong(1));
        talk.setCreationTime(findById(talk.getId()).getCreationTime());
    }
}
