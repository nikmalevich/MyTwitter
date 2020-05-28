package dao.impl;

import dao.ConnectionPool;
import dao.UserDAO;
import models.Constants;
import models.User;

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

public class UserDAOImpl implements UserDAO {
    private static Logger logger;

    static {
        try {
            LogManager.getLogManager().readConfiguration(UserDAOImpl.class.getClassLoader().getResourceAsStream(Constants.LOGGING_PROPERTIES));

            logger = Logger.getLogger(UserDAOImpl.class.getName());
        } catch (Exception ignored) {
        }
    }

    @Override
    public Optional<User> get(int id) {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT name FROM user WHERE user_id=?")) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                User user = new User();

                user.setId(id);
                user.setName(resultSet.getString("name"));

                return Optional.of(user);
            }
        } catch (SQLException | NamingException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public boolean add(String name) {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO user(name) VALUE (?)")) {
            statement.setString(1, name);

            statement.execute();
        } catch (SQLException | NamingException e) {
            logger.log(Level.SEVERE, e.getMessage());

            return false;
        }

        return true;
    }

    @Override
    public Optional<Integer> getID(String name) {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT user_id FROM user WHERE name=?")) {
            statement.setString(1, name);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(resultSet.getInt("user_id"));
            }

            return Optional.empty();
        } catch (SQLException | NamingException e) {
            logger.log(Level.SEVERE, e.getMessage());

            return Optional.empty();
        }
    }
}
