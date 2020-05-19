package dao;

import models.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    Optional<User> get(int id);
    boolean add(String name);
    List<Integer> getIDs(String name);
}
