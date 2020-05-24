package dao.impl;

import dao.ConnectionPool;
import dao.LikeDAO;
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
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LikeDAOImpl implements LikeDAO {
    private static Logger logger;
    private static UserDAO userDAO;

    static {
        try {
            LogManager.getLogManager().readConfiguration(LikeDAOImpl.class.getClassLoader().getResourceAsStream(Constants.LOGGING_PROPERTIES));

            logger = Logger.getLogger(LikeDAOImpl.class.getName());
        } catch (Exception ignored) {
        }
    }

    public LikeDAOImpl() {
        userDAO = new UserDAOImpl();
    }

    @Override
    public List<User> getByPostID(int postID) {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT user_id FROM post_like WHERE post_id=?")) {
            statement.setInt(1, postID);
            ResultSet resultSet = statement.executeQuery();

            List<User> likeUsers = new ArrayList<>();

            while (resultSet.next()) {
                userDAO.get(resultSet.getInt(Constants.USER_ID)).ifPresent(likeUsers::add);
            }

            return likeUsers;
        } catch (SQLException | NamingException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

        return new ArrayList<>();
    }
}
