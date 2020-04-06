package dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import forms.FilterForm;
import models.Constants;
import models.Post;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class PostDAOImpl implements PostDAO {
    private static List<Post> posts;

    public PostDAOImpl() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat(Constants.DATE_FORMAT));

        try {
            posts = objectMapper.readValue(new File(Constants.PATH), new TypeReference<>() {
            });
        } catch (IOException e) {
            posts = new ArrayList<>();

            System.out.println(e.getMessage());
        }
    }

    @Override
    public Optional<Post> get(Long id) {
        return posts.stream().filter(post -> post.getId().equals(id)).findAny();
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
        if (!get(post.getId()).isPresent()) {
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
    public boolean remove(Long id) {
        if (posts.removeIf(post -> post.getId().equals(id))) {
            saveToFile();

            return true;
        }

        return false;
    }

    private void saveToFile() {
        try {
            FileWriter fileWriter = new FileWriter(Constants.PATH);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(new SimpleDateFormat(Constants.DATE_FORMAT));

            System.out.println(objectMapper.writeValueAsString(posts));

            fileWriter.write(objectMapper.writeValueAsString(posts));

            fileWriter.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
