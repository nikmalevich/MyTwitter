package dao.impl;

import dao.*;
import forms.EditPostForm;
import forms.FilterForm;
import forms.NewPostForm;
import models.Constants;
import models.Post;

import javax.naming.NamingException;
import java.sql.*;
import java.util.Date;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class PostDAOImpl implements PostDAO {
    private static Logger logger;
    private static UserDAO userDAO;
    private static HashTagDAO hashTagDAO;
    private static LikeDAO likeDAO;

    static {
        try {
            LogManager.getLogManager().readConfiguration(PostDAOImpl.class.getClassLoader().getResourceAsStream(Constants.LOGGING_PROPERTIES));

            logger = Logger.getLogger(PostDAOImpl.class.getName());
        } catch (Exception ignored) {
        }
    }

    public PostDAOImpl() {
        userDAO = new UserDAOImpl();
        hashTagDAO = new HashTagDAOImpl();
        likeDAO = new LikeDAOImpl();
    }

    @Override
    public Optional<Post> get(int id) {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM post WHERE post_id=?")) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Post post = new Post();

                post.setId(id);
                post.setDescription(resultSet.getString(Constants.DESCRIPTION));
                post.setCreatedAt(new Date(resultSet.getTimestamp("created_at").getTime()));
                userDAO.get(resultSet.getInt(Constants.USER_ID)).ifPresent(post::setAuthor);
                post.setPhotoLink(resultSet.getString("photo_link"));
                post.setHashTags(hashTagDAO.getByPostID(id));
                post.setLikes(likeDAO.getByPostID(id));

                return Optional.of(post);
            }
        } catch (SQLException | NamingException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public int countPosts() {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT count(*) FROM post")) {
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException | NamingException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

        return 0;
    }

    @Override
    public List<Integer> getIDsByDate(Date FROMDate, Date toDate) {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement
                     ("SELECT post_id FROM post WHERE created_at>? AND created_at<?")) {
            statement.setTimestamp(1, new Timestamp(FROMDate.getTime()));
            statement.setTimestamp(2, new Timestamp(toDate.getTime()));

            ResultSet resultSet = statement.executeQuery();

            List<Integer> ids = new ArrayList<>();

            while (resultSet.next()) {
                ids.add(resultSet.getInt(Constants.POST_ID));
            }

            return ids;
        } catch (SQLException | NamingException e) {
            logger.log(Level.SEVERE, e.getMessage());

            return new ArrayList<>();
        }
    }

    @Override
    public List<Integer> getIDsByUserDate(int userID, Date FROMDate, Date toDate) {
        List<Integer> postsID = new ArrayList<>();

        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement
                     ("SELECT post_id FROM post WHERE user_id=? AND created_at>? AND created_at<?")) {
            statement.setInt(1, userID);
            statement.setTimestamp(2, new Timestamp(FROMDate.getTime()));
            statement.setTimestamp(3, new Timestamp(toDate.getTime()));

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                postsID.add(resultSet.getInt(Constants.POST_ID));
            }
        } catch(SQLException | NamingException e){
            logger.log(Level.SEVERE, e.getMessage());

            return postsID;
        }

        return postsID;
    }

    @Override
    public List<Integer> getIDsByHashTag(Integer hashTagID) {
        List<Integer> postsID = new ArrayList<>();

        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT post_id FROM post_tag WHERE tag_id=?")) {
            statement.setInt(1, hashTagID);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                postsID.add(resultSet.getInt(Constants.POST_ID));
            }
        } catch(SQLException | NamingException e){
            logger.log(Level.SEVERE, e.getMessage());

            return postsID;
        }

        return postsID;
    }

    @Override
    public List<Integer> getIDsByHashTags(List<Integer> hashTagsID) {
        List<Integer> postsID = new ArrayList<>();

        for (int hashTagID : hashTagsID) {
            List<Integer> curPostsID = getIDsByHashTag(hashTagID);

            if (postsID.isEmpty()) {
                postsID = curPostsID;
            } else {
                postsID.retainAll(curPostsID);
            }
        }

        return postsID;
    }

    @Override
    public List<Post> getPage(FilterForm filterForm) {
        List<Integer> postsIDByUsersDate;

        if (filterForm.getAuthor() != null) {
            Optional<Integer> userID = userDAO.getID(filterForm.getAuthor());

            if (userID.isEmpty()) {
                return new ArrayList<>();
            }

            postsIDByUsersDate = getIDsByUserDate(userID.get(), filterForm.getFromDate(), filterForm.getToDate());
        } else {
            postsIDByUsersDate = getIDsByDate(filterForm.getFromDate(), filterForm.getToDate());
        }

        if (!filterForm.getDescriptionHashTags().isEmpty()) {
            List<Integer> hashTagsId = hashTagDAO.getByDescriptions(filterForm.getDescriptionHashTags());
            List<Integer> postsIDByHashTags = getIDsByHashTags(hashTagsId);

            postsIDByUsersDate.retainAll(postsIDByHashTags);
        }

        List<Integer> postsID = postsIDByUsersDate;
        List<Post> posts = new ArrayList<>();

        for (int postID : postsID) {
            get(postID).ifPresent(posts::add);
        }

        posts.sort(Comparator.comparing(Post::getCreatedAt));

        if (filterForm.getQuantity() < posts.size()) {
            return posts.subList(0, filterForm.getQuantity());
        } else {
            return posts;
        }
    }

    @Override
    public boolean add(NewPostForm form) {
        Connection connection = null;

        try {
            connection = ConnectionPool.getInstance().getConnection();
            connection.setAutoCommit(false);

            PreparedStatement statement = connection.prepareStatement("SELECT max(post_id) FROM post");
            ResultSet resultSet = statement.executeQuery();

            int postID = 0;

            if (resultSet.next()) {
                postID = resultSet.getInt(1) + 1;
            }

            statement = connection.prepareStatement
                    ("INSERT INTO post(user_id, description, created_at, photo_link) VALUE (?, ?, ?, ?)");
            statement.setInt(1, form.getAuthor().getId());
            statement.setString(2, form.getDescription());
            statement.setTimestamp(3, new Timestamp(new Date().getTime()));

            if (form.getPhotoLink() != null) {
                statement.setString(4, form.getPhotoLink());
            } else {
                statement.setNull(4, 0);
            }

            statement.execute();

            for (String descriptionHashTag : form.getDescriptionHashTags()) {
                statement = connection.prepareStatement("SELECT tag_id FROM tag WHERE description=?");
                statement.setString(1, descriptionHashTag);
                resultSet = statement.executeQuery();

                int tagID = 0;

                if (!resultSet.next()) {
                    statement = connection.prepareStatement("SELECT max(tag_id) FROM tag");
                    resultSet = statement.executeQuery();

                    if (resultSet.next()) {
                        tagID = resultSet.getInt(1) + 1;
                    }

                    statement = connection.prepareStatement("INSERT INTO tag(description) VALUE(?)");
                    statement.setString(1, descriptionHashTag);

                    statement.execute();
                } else {
                    tagID = resultSet.getInt(Constants.TAG_ID);
                }

                statement = connection.prepareStatement("INSERT INTO post_tag(post_id, tag_id) VALUE(?, ?)");
                statement.setInt(1, postID);
                statement.setInt(2, tagID);

                statement.execute();
            }

            connection.commit();
        } catch (SQLException | NamingException e1) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException e2) {
                logger.log(Level.SEVERE, e2.getMessage());
            }

            logger.log(Level.SEVERE, e1.getMessage());

            return false;
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        }

        return true;
    }

    @Override
    public boolean edit(EditPostForm form) {
        Connection connection = null;

        try {
            connection = ConnectionPool.getInstance().getConnection();
            connection.setAutoCommit(false);

            PreparedStatement statement = connection.prepareStatement
                    ("UPDATE post SET description=?, photo_link=? WHERE post_id=?");
            statement.setString(1, form.getDescription());

            if (form.getPhotoLink() != null) {
                statement.setString(2, form.getPhotoLink());
            } else {
                statement.setNull(2, 0);
            }

            statement.setInt(3, form.getId());

            statement.execute();

            statement = connection.prepareStatement("DELETE FROM post_tag WHERE post_id=?");
            statement.setInt(1, form.getId());

            statement.execute();

            for (String descriptionHashTag : form.getDescriptionHashTags()) {
                statement = connection.prepareStatement("SELECT tag_id FROM tag WHERE description=?");
                statement.setString(1, descriptionHashTag);
                ResultSet resultSet = statement.executeQuery();

                int tagID = 0;

                if (!resultSet.next()) {
                    statement = connection.prepareStatement("SELECT max(tag_id) FROM tag");
                    resultSet = statement.executeQuery();

                    if (resultSet.next()) {
                        tagID = resultSet.getInt(1) + 1;
                    }

                    statement = connection.prepareStatement("INSERT INTO tag(description) VALUE(?)");
                    statement.setString(1, descriptionHashTag);

                    statement.execute();
                } else {
                    tagID = resultSet.getInt(Constants.TAG_ID);
                }

                statement = connection.prepareStatement("INSERT INTO post_tag(post_id, tag_id) VALUE(?, ?)");
                statement.setInt(1, form.getId());
                statement.setInt(2, tagID);

                statement.execute();
            }

            connection.commit();
        } catch (SQLException | NamingException e1) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException e2) {
                logger.log(Level.SEVERE, e2.getMessage());
            }

            logger.log(Level.SEVERE, e1.getMessage());

            return false;
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        }

        return true;
    }

    @Override
    public boolean remove(int id) {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM post WHERE post_id=?")) {
            statement.setInt(1, id);

            statement.execute();
        } catch (SQLException | NamingException e) {
            logger.log(Level.SEVERE, e.getMessage());

            return false;
        }

        return true;
    }

    @Override
    public boolean like(int postID, int userID) {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement
                     ("INSERT INTO post_like(post_id, user_id) VALUE(?, ?)")) {
            statement.setInt(1, postID);
            statement.setInt(2, userID);

            statement.execute();
        } catch (SQLException | NamingException e) {
            logger.log(Level.SEVERE, e.getMessage());

            return false;
        }

        return true;
    }

    @Override
    public boolean dislike(int postID, int userID) {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement
                     ("DELETE FROM post_like WHERE post_id=? AND user_id=?")) {
            statement.setInt(1, postID);
            statement.setInt(2, userID);

            statement.execute();
        } catch (SQLException | NamingException e) {
            logger.log(Level.SEVERE, e.getMessage());

            return false;
        }

        return true;
    }
}
