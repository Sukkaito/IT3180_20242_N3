package vn.edu.hust.nmcnpm_20242_n3.dto;

public class CategoryDTO {

    private String name;

    // Constructors
    public CategoryDTO() {
    }

    public CategoryDTO(String name) {
        this.name = name;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}