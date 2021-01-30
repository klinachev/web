package ru.itmo.wp.model.repository.impl;
import ru.itmo.wp.model.domain.Event;
import ru.itmo.wp.model.repository.EventRepository;

import java.sql.*;
import java.util.List;

public class EventRepositoryImpl extends BaseRepositoryImpl<Event> implements EventRepository {
    public EventRepositoryImpl() {
        super("Event");
    }

    @Override
    public Event toObject(ResultSetMetaData resultSetMetaData, ResultSet resultSet) throws SQLException {
        return toEvent(resultSetMetaData, resultSet);
    }

    private Event toEvent(ResultSetMetaData metaData, ResultSet resultSet) throws SQLException {
        if (!resultSet.next()) {
            return null;
        }

        Event event = new Event();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            switch (metaData.getColumnName(i)) {
                case "id":
                    event.setId(resultSet.getLong(i));
                    break;
                case "userId":
                    event.setUserId(resultSet.getLong(i));
                    break;
                case "type":
                    event.setType(Event.Type.valueOf(resultSet.getString(i)));
                    break;
                case "creationTime":
                    event.setCreationTime(resultSet.getTimestamp(i));
                    break;
                default:
                    // No operations.
            }
        }

        return event;
    }

    @Override
    public void save(Event event) {
        List<Object> parameters = List.of(event);
        save("(`userId`, `type`, `creationTime`)", "(?, ?, NOW())", parameters);
    }

    @Override
    protected void saveMethodSetParameters(PreparedStatement statement, List<Object> parameters) throws SQLException {
        Event event = (Event) parameters.get(0);
        statement.setLong(1, event.getUserId());
        statement.setString(2, String.valueOf(event.getType()));
    }

    @Override
    protected void saveMethodSetGeneratedKeys(ResultSet generatedKeys, List<Object> parameters) throws SQLException {
        Event event = (Event) parameters.get(0);
        event.setId(generatedKeys.getLong(1));
        event.setCreationTime(findById(event.getId()).getCreationTime());
    }

}
