package dao;

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
    private static ConnectionPool connectionPool;

    static {
        try {
            LogManager.getLogManager().readConfiguration(HashTagDAOImpl.class.getClassLoader().getResourceAsStream("logging.properties"));

            logger = Logger.getLogger(HashTagDAOImpl.class.getName());
        } catch (Exception ignored) {
        }
    }

    public HashTagDAOImpl() {
        connectionPool = ConnectionPool.getInstance();
    }

    @Override
    public Optional<HashTag> get(int id) {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("select description from tag where tag_id=?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                HashTag hashTag = new HashTag();

                hashTag.setId(id);
                hashTag.setDescription(resultSet.getString("description"));

                return Optional.of(hashTag);
            }
        } catch (SQLException | NamingException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public List<HashTag> getByPostID(int postID) {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("select tag_id from post_tag where post_id=?");
            statement.setInt(1, postID);
            ResultSet resultSet = statement.executeQuery();

            List<HashTag> hashTags = new ArrayList<>();

            if (resultSet.next()) {
                get(resultSet.getInt("tag_id")).ifPresent(hashTags::add);
            }

            return hashTags;
        } catch (SQLException | NamingException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

        return new ArrayList<>();
    }

    @Override
    public Optional<Integer> getByDescription(String description) {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("select tag_id from tag where description=?");
            statement.setString(1, description);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(resultSet.getInt("tag_id"));
            }
        } catch (SQLException | NamingException e) {
            logger.log(Level.SEVERE, e.getMessage());

            return Optional.empty();
        }

        return Optional.empty();
    }

    @Override
    public List<Integer> getByDescriptions(List<String> descriptions) {
        List<Integer> IDs = new ArrayList<>();

        for (String description : descriptions) {
            getByDescription(description).ifPresent(IDs::add);
        }

        return IDs;
    }
}
