package dao;

import forms.EditPostForm;
import forms.FilterForm;
import forms.NewPostForm;
import models.Post;

import javax.naming.NamingException;
import java.io.FileInputStream;
import java.sql.*;
import java.util.Date;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class PostDAOImpl implements PostDAO {
    private static Logger logger;
    private static ConnectionPool connectionPool;
    private static UserDAO userDAO;
    private static HashTagDAO hashTagDAO;
    private static LikeDAO likeDAO;

    static {
        try {
            LogManager.getLogManager().readConfiguration(new FileInputStream("D:\\GitHub\\MyTwitter\\task8\\TutterApp\\src\\main\\webapp\\resources\\logging.properties"));

            logger = Logger.getLogger(PostDAOImpl.class.getName());
        } catch (Exception ignored) {
        }
    }

    public PostDAOImpl() {
        connectionPool = ConnectionPool.getInstance();
        userDAO = new UserDAOImpl();
        hashTagDAO = new HashTagDAOImpl();
        likeDAO = new LikeDAOImpl();
    }

    @Override
    public Optional<Post> get(int id) {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("select * from post where post_id=?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Post post = new Post();

                post.setId(id);
                post.setDescription(resultSet.getString("description"));
                post.setCreatedAt(new Date(resultSet.getTimestamp("created_at").getTime()));
                userDAO.get(resultSet.getInt("user_id")).ifPresent(post::setAuthor);
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
    public List<Integer> getIDsByDate(Date fromDate, Date toDate) {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement
                    ("select post_id from post where created_at>? and created_at<?");
            statement.setTimestamp(1, new Timestamp(fromDate.getTime()));
            statement.setTimestamp(2, new Timestamp(toDate.getTime()));

            ResultSet resultSet = statement.executeQuery();

            List<Integer> IDs = new ArrayList<>();

            while (resultSet.next()) {
                IDs.add(resultSet.getInt("post_id"));
            }

            return IDs;
        } catch (SQLException | NamingException e) {
            logger.log(Level.SEVERE, e.getMessage());

            return new ArrayList<>();
        }
    }

    @Override
    public List<Integer> getIDsByUsersDate(List<Integer> usersID, Date fromDate, Date toDate) {
        List<Integer> postsID = new ArrayList<>();

        for (int userID : usersID) {
            try (Connection connection = connectionPool.getConnection()) {
                PreparedStatement statement = connection.prepareStatement
                        ("select post_id from post where user_id=? and created_at>? and created_at<?");
                statement.setInt(1, userID);
                statement.setTimestamp(2, new Timestamp(fromDate.getTime()));
                statement.setTimestamp(3, new Timestamp(toDate.getTime()));

                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    postsID.add(resultSet.getInt("post_id"));
                }
            } catch(SQLException | NamingException e){
                logger.log(Level.SEVERE, e.getMessage());

                return postsID;
            }
        }

        return postsID;
    }

    @Override
    public List<Integer> getIDsByHashTag(Integer hashTagID) {
        List<Integer> postsID = new ArrayList<>();

        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement
                    ("select post_id from post_tag where tag_id=?");
            statement.setInt(1, hashTagID);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                postsID.add(resultSet.getInt("post_id"));
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

            postsID.retainAll(curPostsID);
        }

        return postsID;
    }

    @Override
    public List<Post> getPage(FilterForm filterForm) {
        List<Integer> usersID;

        if (filterForm.getAuthor() != null) {
            usersID = userDAO.getIDs(filterForm.getAuthor());
        } else {
            usersID = new ArrayList<>();
        }

        List<Integer> postsIDByUsersDate;

        if (usersID.isEmpty()) {
            postsIDByUsersDate = getIDsByDate(filterForm.getFromDate(), filterForm.getToDate());
        } else {
            postsIDByUsersDate = getIDsByUsersDate(usersID, filterForm.getFromDate(), filterForm.getToDate());
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
            connection = connectionPool.getConnection();
            connection.setAutoCommit(false);

            PreparedStatement statement = connection.prepareStatement("select max(post_id) from post");
            ResultSet resultSet = statement.executeQuery();

            int postID = 0;

            if (resultSet.next()) {
                postID = resultSet.getInt(1) + 1;
            }

            statement = connection.prepareStatement
                    ("insert into post(user_id, description, created_at, photo_link) value (?, ?, ?, ?)");
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
                statement = connection.prepareStatement("select tag_id from tag where description=?");
                statement.setString(1, descriptionHashTag);
                resultSet = statement.executeQuery();

                int tagID = 0;

                if (!resultSet.next()) {
                    statement = connection.prepareStatement("select max(tag_id) from tag");
                    resultSet = statement.executeQuery();

                    if (resultSet.next()) {
                        tagID = resultSet.getInt(1) + 1;
                    }

                    statement = connection.prepareStatement("insert into tag(description) value(?)");
                    statement.setString(1, descriptionHashTag);

                    statement.execute();
                } else {
                    tagID = resultSet.getInt("tag_id");
                }

                statement = connection.prepareStatement("insert into post_tag(post_id, tag_id) value(?, ?)");
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
            connection = connectionPool.getConnection();
            connection.setAutoCommit(false);

            PreparedStatement statement = connection.prepareStatement
                    ("update post set description=?, photo_link=? where post_id=?");
            statement.setString(1, form.getDescription());

            if (form.getPhotoLink() != null) {
                statement.setString(2, form.getPhotoLink());
            } else {
                statement.setNull(2, 0);
            }

            statement.setInt(3, form.getId());

            statement.execute();

            statement = connection.prepareStatement("delete from post_tag where post_id=?");
            statement.setInt(1, form.getId());

            statement.execute();

            for (String descriptionHashTag : form.getDescriptionHashTags()) {
                statement = connection.prepareStatement("select tag_id from tag where description=?");
                statement.setString(1, descriptionHashTag);
                ResultSet resultSet = statement.executeQuery();

                int tagID = 0;

                if (!resultSet.next()) {
                    statement = connection.prepareStatement("select max(tag_id) from tag");
                    resultSet = statement.executeQuery();

                    if (resultSet.next()) {
                        tagID = resultSet.getInt(1) + 1;
                    }

                    statement = connection.prepareStatement("insert into tag(description) value(?)");
                    statement.setString(1, descriptionHashTag);

                    statement.execute();
                } else {
                    tagID = resultSet.getInt("tag_id");
                }

                statement = connection.prepareStatement("insert into post_tag(post_id, tag_id) value(?, ?)");
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
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("delete from post where post_id=?");
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
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement
                    ("insert into post_like(post_id, user_id) value(?, ?)");
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
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement
                    ("delete from post_like where post_id=? and user_id=?");
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
