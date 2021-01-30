package ru.itmo.wp.model.repository.impl;

import ru.itmo.wp.model.database.DatabaseUtils;
import ru.itmo.wp.model.exception.RepositoryException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

abstract public class BaseRepositoryImpl<T> {
    protected final DataSource DATA_SOURCE = DatabaseUtils.getDataSource();
    protected final String TABLE_NAME;

    protected BaseRepositoryImpl(String table_name) {
        TABLE_NAME = table_name;
    }

    protected class DbState implements AutoCloseable {
        public Connection connection = null;
        public PreparedStatement statement = null;

        public Connection getConnection() {
            return connection;
        }

        public PreparedStatement getStatement() {
            return statement;
        }

        DbState(String arg) throws SQLException {
            open(arg);
        }

        private void open(String arg) throws SQLException {
            connection = DATA_SOURCE.getConnection();
            try {
                statement = connection.prepareStatement(arg, Statement.RETURN_GENERATED_KEYS);
            } catch (SQLException e) {
                connection.close();
                throw e;
            }
        }

        public void close() throws SQLException {
            statement.close();
            connection.close();
        }
    }

    public boolean isExist(String name) {
        try (DbState dbState = new DbState("SELECT EXISTS(SELECT id FROM " + TABLE_NAME + " WHERE login=?)")) {
            PreparedStatement statement = dbState.getStatement();
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBoolean(1);
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find object.", e);
        }
        return false;
    }

    public long findCount() {
        try (DbState dbState = new DbState("SELECT COUNT(*) FROM " + TABLE_NAME)) {
            PreparedStatement statement = dbState.getStatement();
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong(1);
                }
                throw new RepositoryException("Can't count objects.");
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't count objects.", e);
        }
    }

    protected T findByString(String argName, String arg) {
        try (DbState dbState = new DbState("SELECT * FROM " + TABLE_NAME +" WHERE " + argName + "=?")) {
            PreparedStatement statement = dbState.getStatement();
            statement.setString(1, arg);
            try (ResultSet resultSet = statement.executeQuery()) {
                return toObject(statement.getMetaData(), resultSet);
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find object.", e);
        }
    }

    protected List<T> findAll(String parameters) {
        List<T> objects = new ArrayList<>();
        try (DbState dbState = new DbState("SELECT * FROM " + TABLE_NAME + " " + parameters)) {
            PreparedStatement statement = dbState.getStatement();
            try (ResultSet resultSet = statement.executeQuery()) {
                T talk;
                while ((talk = toObject(statement.getMetaData(), resultSet)) != null) {
                    objects.add(talk);
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find object.", e);
        }
        return objects;
    }

    public T findById(long id) {
        try (DbState dbState = new DbState("SELECT * FROM " + TABLE_NAME + " WHERE id=?")) {
            PreparedStatement statement = dbState.getStatement();
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return toObject(statement.getMetaData(), resultSet);
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find object.", e);
        }
    }

    protected abstract T toObject(ResultSetMetaData resultSetMetaData, ResultSet resultSet) throws SQLException;

    protected void save(String tableParameters, String tableArgs, List<Object> parameters) {
        try (DbState dbState = new DbState("INSERT INTO " + TABLE_NAME + ' ' +
                tableParameters + " VALUES " + tableArgs)) {
            PreparedStatement statement = dbState.getStatement();
            saveMethodSetParameters(statement, parameters);
            if (statement.executeUpdate() != 1) {
                throw new RepositoryException("Can't save object.");
            } else {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    saveMethodSetGeneratedKeys(generatedKeys, parameters);
                } else {
                    throw new RepositoryException("Can't save object [no autogenerated fields].");
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't save object.", e);
        }
    }

    protected abstract void saveMethodSetParameters(PreparedStatement statement, List<Object> parameters) throws SQLException;

    protected abstract void saveMethodSetGeneratedKeys(ResultSet generatedKeys, List<Object> parameters) throws SQLException;
}
