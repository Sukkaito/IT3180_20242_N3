package vn.edu.hust.nmcnpm_20242_n3.entity;

import jakarta.persistence.*;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookCopyStatusEnum;

@Entity
@Table(name = "book_copies")
public class BookCopy {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    private Book originalBook;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BookCopyStatusEnum status;

    // Getter and Setter methods
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Book getOriginalBook() {
        return originalBook;
    }

    public void setOriginalBook(Book originalBook) {
        this.originalBook = originalBook;
    }

    public BookCopyStatusEnum getStatus() {
        return status;
    }

    public void setStatus(BookCopyStatusEnum status) {
        this.status = status;
    }
}
