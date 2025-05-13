package vn.edu.hust.nmcnpm_20242_n3.dto;

public class BookBrowsingDTO extends BookDTO {
    private int availableCopies;

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }
}
