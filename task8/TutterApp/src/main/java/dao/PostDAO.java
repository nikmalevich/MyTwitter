package dao;

import forms.FilterForm;
import models.Post;

import java.util.List;
import java.util.Optional;

public interface PostDAO {
    Optional<Post> get(long id);
    List<Post> getPage(FilterForm filterForm);
    boolean add(Post post);
    boolean edit(Post post);
    boolean remove(long id);
}