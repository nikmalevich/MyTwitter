package forms;

import models.User;

import java.util.List;
import java.util.Objects;

public class NewPostForm {
    private String description;
    private User author;
    private List<String> descriptionHashTags;
    private String photoLink;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public List<String> getDescriptionHashTags() {
        return descriptionHashTags;
    }

    public void setDescriptionHashTags(List<String> descriptionHashTags) {
        this.descriptionHashTags = descriptionHashTags;
    }

    public String getPhotoLink() {
        return photoLink;
    }

    public void setPhotoLink(String photoLink) {
        this.photoLink = photoLink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NewPostForm)) return false;
        NewPostForm newPostForm = (NewPostForm) o;
        return Objects.equals(description, newPostForm.description) &&
                Objects.equals(author, newPostForm.author) &&
                Objects.equals(descriptionHashTags, newPostForm.descriptionHashTags) &&
                Objects.equals(photoLink, newPostForm.photoLink);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, author, descriptionHashTags, photoLink);
    }
}
