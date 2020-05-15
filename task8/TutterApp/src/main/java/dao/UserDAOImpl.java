package dao;

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
    private static ConnectionPool connectionPool;

    static {
        try {
            LogManager.getLogManager().readConfiguration(UserDAOImpl.class.getClassLoader().getResourceAsStream("logging.properties"));

            logger = Logger.getLogger(UserDAOImpl.class.getName());
        } catch (Exception ignored) {
        }
    }

    public UserDAOImpl() {
        connectionPool = ConnectionPool.getInstance();
    }

    @Override
    public Optional<User> get(int id) {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("select name from user where user_id=?");
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
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("insert into user(name) value (?)");
            statement.setString(1, name);

            statement.execute();
        } catch (SQLException | NamingException e) {
            logger.log(Level.SEVERE, e.getMessage());

            return false;
        }

        return true;
    }

    @Override
    public List<Integer> getIDs(String name) {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("select user_id from user where name=?");
            statement.setString(1, name);

            ResultSet resultSet = statement.executeQuery();

            List<Integer> IDs = new ArrayList<>();

            while (resultSet.next()) {
                IDs.add(resultSet.getInt("user_id"));
            }

            return IDs;
        } catch (SQLException | NamingException e) {
            logger.log(Level.SEVERE, e.getMessage());

            return new ArrayList<>();
        }
    }
}
