package forms;

import java.util.List;
import java.util.Objects;

public class EditPostForm {
    private int id;
    private String description;
    private List<String> descriptionHashTags;
    private String photoLink;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        if (!(o instanceof EditPostForm)) return false;
        EditPostForm that = (EditPostForm) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
