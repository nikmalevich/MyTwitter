package forms;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class FilterForm {
    private String author;
    private List<String> hashTags;
    private Date fromDate;
    private Date toDate;
    private int skip;
    private int quantity;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<String> getHashTags() {
        return hashTags;
    }

    public void setHashTags(List<String> hashTags) {
        this.hashTags = hashTags;
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

    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
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
        return skip == that.skip &&
                quantity == that.quantity &&
                Objects.equals(author, that.author) &&
                Objects.equals(hashTags, that.hashTags) &&
                Objects.equals(fromDate, that.fromDate) &&
                Objects.equals(toDate, that.toDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, hashTags, fromDate, toDate, skip, quantity);
    }
}
