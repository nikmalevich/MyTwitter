package dao;

import models.User;

import java.util.List;

public interface LikeDAO {
    List<User> getByPostID(int postID);
}
