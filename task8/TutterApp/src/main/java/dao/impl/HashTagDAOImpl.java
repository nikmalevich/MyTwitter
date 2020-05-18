package dao.impl;

import dao.ConnectionPool;
import dao.HashTagDAO;
import models.Constants;
import models.HashTag;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class HashTagDAOImpl implements HashTagDAO {
    private static Logger logger;

    static {
        try {
            LogManager.getLogManager().readConfiguration(HashTagDAOImpl.class.getClassLoader().getResourceAsStream(Constants.LOGGING_PROPERTIES));

            logger = Logger.getLogger(HashTagDAOImpl.class.getName());
        } catch (Exception ignored) {
        }
    }

    @Override
    public Optional<HashTag> get(int id) {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT description FROM tag WHERE tag_id=?")) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                HashTag hashTag = new HashTag();

                hashTag.setId(id);
                hashTag.setDescription(resultSet.getString(Constants.DESCRIPTION));

                return Optional.of(hashTag);
            }
        } catch (SQLException | NamingException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public List<HashTag> getByPostID(int postID) {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT tag_id FROM post_tag WHERE post_id=?")) {
            statement.setInt(1, postID);
            ResultSet resultSet = statement.executeQuery();

            List<HashTag> hashTags = new ArrayList<>();

            if (resultSet.next()) {
                get(resultSet.getInt(Constants.TAG_ID)).ifPresent(hashTags::add);
            }

            return hashTags;
        } catch (SQLException | NamingException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

        return new ArrayList<>();
    }

    @Override
    public Optional<Integer> getByDescription(String description) {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT tag_id FROM tag WHERE description=?")) {
            statement.setString(1, description);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(resultSet.getInt(Constants.TAG_ID));
            }
        } catch (SQLException | NamingException e) {
            logger.log(Level.SEVERE, e.getMessage());

            return Optional.empty();
        }

        return Optional.empty();
    }

    @Override
    public List<Integer> getByDescriptions(List<String> descriptions) {
        List<Integer> ids = new ArrayList<>();

        for (String description : descriptions) {
            getByDescription(description).ifPresent(ids::add);
        }

        return ids;
    }
}
