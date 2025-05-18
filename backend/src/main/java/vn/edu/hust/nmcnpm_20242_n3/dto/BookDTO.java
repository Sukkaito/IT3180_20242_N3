package vn.edu.hust.nmcnpm_20242_n3.dto;

import java.util.Set;

public class BookDTO {

    private int id;
    private String title;
    private String description;
    private int publisherId;
    private Set<Integer> authorIds;
    private Set<Integer> categoryIds;

    // Constructors
    public BookDTO() {
    }

    public BookDTO(int id, String title, String description, int publisherId, Set<Integer> authorIds, Set<Integer> categoryIds) {
        this.title = title;
        this.description = description;
        this.publisherId = publisherId;
        this.authorIds = authorIds;
        this.categoryIds = categoryIds;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public int getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(int publisherId) {
        this.publisherId = publisherId;
    }

    public Set<Integer> getAuthorIds() {
        return authorIds;
    }

    public void setAuthorIds(Set<Integer> authorIds) {
        this.authorIds = authorIds;
    }

    public Set<Integer> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(Set<Integer> categoryIds) {
        this.categoryIds = categoryIds;
    }
}