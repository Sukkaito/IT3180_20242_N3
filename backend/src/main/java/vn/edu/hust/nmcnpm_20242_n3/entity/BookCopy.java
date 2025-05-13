package vn.edu.hust.nmcnpm_20242_n3.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookCopyStatusEnum;

// Needed for HTTP requests and responses
@Setter
@Getter

@Entity
@Table(name = "book_copies")
public class BookCopy {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    Book originalBook;

    @Getter
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    BookCopyStatusEnum status;

    public Object orElseThrow(Object o) {
        return o;
    }

    @PrePersist
    protected void onCreate() {
        status = BookCopyStatusEnum.AVAILABLE;
    }


}