package dao;

import models.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    Optional<User> get(int id);
    Optional<Integer> getID(String name);
    Optional<Integer> login(String name, String password);
}
