package vn.edu.hust.nmcnpm_20242_n3.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookCopyStatusEnum;

import java.util.HashSet;
import java.util.Set;

// Needed for HTTP requests and responses
@Setter
@Getter

@Entity
@Table(name = "book_copies")
public class BookCopy {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    private Book originalBook;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BookCopyStatusEnum status;

    @OneToMany(mappedBy = "bookCopy", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<BookLoan> bookLoans = new HashSet<>();

    @OneToMany(mappedBy = "bookCopy", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<BookRequest> bookRequests = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        status = BookCopyStatusEnum.AVAILABLE;
    }
}