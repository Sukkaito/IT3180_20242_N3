package vn.edu.hust.nmcnpm_20242_n3.dto;

import java.util.Set;

public class BookDTO {

    private String title;
    private String description;
    private String publisherName;
    private Set<String> authorNames;
    private Set<String> categoryNames;

    // Constructors
    public BookDTO() {
    }

    public BookDTO(String title, String description, String publisherName,
                   Set<String> authorNames, Set<String> categoryNames) {
        this.title = title;
        this.description = description;
        this.publisherName = publisherName;
        this.authorNames = authorNames;
        this.categoryNames = categoryNames;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public Set<String> getAuthorNames() {
        return authorNames;
    }

    public void setAuthorNames(Set<String> authorNames) {
        this.authorNames = authorNames;
    }

    public Set<String> getCategoryNames() {
        return categoryNames;
    }

    public void setCategoryNames(Set<String> categoryNames) {
        this.categoryNames = categoryNames;
    }
}