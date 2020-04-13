package dao;

import com.fasterxml.jackson.core.type.TypeReference;
import forms.FilterForm;
import models.Constants;
import models.Post;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PostDAOImpl implements PostDAO {
    private static List<Post> posts;
    private static Logger logger;

    static {
        try {
            LogManager.getLogManager().readConfiguration(new FileInputStream("D:\\GitHub\\MyTwitter\\task8\\TutterApp\\src\\main\\resources\\log.config"));

            logger = Logger.getLogger(PostDAOImpl.class.getName());
        } catch (Exception ignored) {
        }
    }

    public PostDAOImpl() {
        try {
            posts = Constants.objectMapper.readValue(new File(Constants.PATH), new TypeReference<>() {
            });
        } catch (IOException e) {
            posts = new ArrayList<>();

           logger.log(Level.SEVERE, e.getMessage());
        }
    }

    @Override
    public Optional<Post> get(long id) {
        return posts.stream().filter(post -> post.getId() == id).findAny();
    }

    @Override
    public List<Post> getPage(FilterForm filterForm) {
        return posts.stream().filter(post -> (filterForm.getAuthor() == null || filterForm.getAuthor().equals(post.getAuthor())) &&
                (filterForm.getHashTags() == null || filterForm.getHashTags().equals(post.getHashTags())) &&
                (filterForm.getFromDate() == null || post.getCreatedAt().after(filterForm.getFromDate())) &&
                (filterForm.getToDate() == null || post.getCreatedAt().before(filterForm.getToDate()))).
                sorted(Comparator.comparing(Post::getCreatedAt)).
                skip(filterForm.getSkip()).
                limit(filterForm.getQuantity()).
                collect(Collectors.toList());
    }

    @Override
    public boolean add(Post post) {
        if (get(post.getId()).isEmpty()) {
            posts.add(post);
            saveToFile();

            return true;
        }

        return false;
    }

    @Override
    public boolean edit(Post post) {
        Optional<Post> optionalEditPost = get(post.getId());

        if (optionalEditPost.isPresent()) {
            Post editPost = optionalEditPost.get();

            editPost.setDescription(Objects.requireNonNullElse(post.getDescription(), editPost.getDescription()));

            if (post.getPhotoLink() != null) {
                editPost.setPhotoLink(post.getPhotoLink());
            }

            editPost.setHashTags(Objects.requireNonNullElse(post.getHashTags(), editPost.getHashTags()));
            editPost.setLikes(Objects.requireNonNullElse(post.getLikes(), editPost.getLikes()));

            saveToFile();

            return true;
        }

        return false;
    }

    @Override
    public boolean remove(long id) {
        if (posts.removeIf(post -> post.getId() == id)) {
            saveToFile();

            return true;
        }

        return false;
    }

    private void saveToFile() {
        try {
            FileWriter fileWriter = new FileWriter(Constants.PATH);

            fileWriter.write(Constants.objectMapper.writeValueAsString(posts));

            fileWriter.close();
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
}
