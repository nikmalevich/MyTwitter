package dao;

import forms.FilterForm;
import forms.EditPostForm;
import forms.NewPostForm;
import models.Post;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PostDAO {
    Optional<Post> get(int id);
    int countPosts();
    List<Integer> getIDsByDate(Date fromDate, Date toDate);
    List<Integer> getIDsByUserDate(int usersID, Date fromDate, Date toDate);
    List<Integer> getIDsByHashTag(Integer hashTagID);
    List<Integer> getIDsByHashTags(List<Integer> hashTagsID);
    List<Post> getPage(FilterForm filterForm);
    Optional<Integer> getUserID(int id);
    boolean add(NewPostForm form);
    boolean edit(EditPostForm post);
    boolean remove(int id);
    boolean like(int postID, int userID);
    boolean dislike(int postID, int userID);
}