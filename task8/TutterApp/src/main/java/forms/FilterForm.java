package forms;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class FilterForm {
    private String author;
    private List<String> descriptionHashTags;
    private Date fromDate;
    private Date toDate;
    private int quantity;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<String> getDescriptionHashTags() {
        return descriptionHashTags;
    }

    public void setDescriptionHashTags(List<String> descriptionHashTags) {
        this.descriptionHashTags = descriptionHashTags;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FilterForm)) return false;
        FilterForm that = (FilterForm) o;
        return quantity == that.quantity &&
                author.equals(that.author) &&
                descriptionHashTags.equals(that.descriptionHashTags) &&
                fromDate.equals(that.fromDate) &&
                toDate.equals(that.toDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, descriptionHashTags, fromDate, toDate, quantity);
    }
}
