package vn.edu.hust.nmcnpm_20242_n3.entity;

import vn.edu.hust.nmcnpm_20242_n3.constant.BookRequestStatusEnum;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookRequestTypeEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Setter
@Getter

@Entity
@Table(name = "book_requests")
public class BookRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(cascade = CascadeType.ALL)
    private BookLoan bookLoan;

    @ManyToOne(cascade = CascadeType.ALL)
    private BookCopy bookCopy;

    @ManyToOne(cascade = CascadeType.ALL)

    private User user;

    @Enumerated(EnumType.STRING)
    private BookRequestStatusEnum status;

    @Enumerated(EnumType.STRING)
    private BookRequestTypeEnum type;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}