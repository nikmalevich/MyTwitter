package dao;

import models.HashTag;

import java.util.List;
import java.util.Optional;

public interface HashTagDAO {
    Optional<HashTag> get(int id);
    List<HashTag> getByPostID(int postID);
    Optional<Integer> getByDescription(String description);
    List<Integer> getByDescriptions(List<String> descriptions);
}
