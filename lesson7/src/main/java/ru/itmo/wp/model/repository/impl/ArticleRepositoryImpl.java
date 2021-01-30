package ru.itmo.wp.model.repository.impl;

import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.exception.RepositoryException;
import ru.itmo.wp.model.repository.ArticleRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArticleRepositoryImpl extends BaseRepositoryImpl<Article> implements ArticleRepository {

    public ArticleRepositoryImpl() {
        super("Article");
    }

    public List<Article> findAllUnhiddenArticles() {
        return super.findAll("WHERE hidden=false ORDER BY creationTime DESC");
    }

    public List<Article> findAllByUserId(long id) {
        List<Article> articles = new ArrayList<>();
        try (DbState dbState = new DbState("SELECT * FROM Article WHERE userId=? ORDER BY creationTime DESC")) {
            PreparedStatement statement = dbState.getStatement();
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                Article talk;
                while ((talk = toObject(statement.getMetaData(), resultSet)) != null) {
                    articles.add(talk);
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find Object.", e);
        }
        return articles;
    }

    @Override
    public void setHidden(long id, boolean val) {
        setBoolean(id, val, "hidden");
    }

    @Override
    protected Article toObject(ResultSetMetaData resultSetMetaData, ResultSet resultSet) throws SQLException {
        return toArticle(resultSetMetaData, resultSet);
    }

    private Article toArticle(ResultSetMetaData metaData, ResultSet resultSet) throws SQLException {
        if (!resultSet.next()) {
            return null;
        }

        Article article = new Article();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            switch (metaData.getColumnName(i)) {
                case "id":
                    article.setId(resultSet.getLong(i));
                    break;
                case "userId":
                    article.setUserId(resultSet.getLong(i));
                    break;
                case "hidden":
                    article.setHidden(resultSet.getBoolean(i));
                    break;
                case "title":
                    article.setTitle(resultSet.getString(i));
                    break;
                case "text":
                    article.setText(resultSet.getString(i));
                    break;
                case "creationTime":
                    article.setCreationTime(resultSet.getTimestamp(i));
                    break;
                default:
                    // No operations.
            }
        }

        return article;
    }

    @Override
    public void save(Article article) {
        List<Object> parameters = List.of(article);
        save(" (`userId`, `title`, `text`, `creationTime`)", "(?, ?, ?, NOW())", parameters);

    }

    @Override
    protected void saveMethodSetParameters(PreparedStatement statement, List<Object> parameters) throws SQLException {
        Article article = (Article) parameters.get(0);
        statement.setLong(1, article.getUserId());
        statement.setString(2, article.getTitle());
        statement.setString(3, article.getText());
    }

    @Override
    protected void saveMethodSetGeneratedKeys(ResultSet generatedKeys, List<Object> parameters) throws SQLException {
        Article article = (Article) parameters.get(0);
        article.setId(generatedKeys.getLong(1));
        article.setCreationTime(findById(article.getId()).getCreationTime());
    }
}
