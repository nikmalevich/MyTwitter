package dao;

import models.User;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LikeDAOImpl implements LikeDAO {
    private static Logger logger;
    private static ConnectionPool connectionPool;
    private static UserDAO userDAO;

    static {
        try {
            LogManager.getLogManager().readConfiguration(LikeDAOImpl.class.getClassLoader().getResourceAsStream("logging.properties"));

            logger = Logger.getLogger(LikeDAOImpl.class.getName());
        } catch (Exception ignored) {
        }
    }

    public LikeDAOImpl() {
        connectionPool = ConnectionPool.getInstance();
        userDAO = new UserDAOImpl();
    }

    @Override
    public List<User> getByPostID(int postID) {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("select user_id from post_like where post_id=?");
            statement.setInt(1, postID);
            ResultSet resultSet = statement.executeQuery();

            List<User> likeUsers = new ArrayList<>();

            if (resultSet.next()) {
                userDAO.get(resultSet.getInt("user_id")).ifPresent(likeUsers::add);
            }

            return likeUsers;
        } catch (SQLException | NamingException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

        return new ArrayList<>();
    }
}
