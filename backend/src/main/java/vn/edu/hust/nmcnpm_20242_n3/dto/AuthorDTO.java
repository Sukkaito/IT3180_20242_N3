package vn.edu.hust.nmcnpm_20242_n3.dto;

public class AuthorDTO {

    private String name;

    // Constructors
    public AuthorDTO() {
    }

    public AuthorDTO(String name) {
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