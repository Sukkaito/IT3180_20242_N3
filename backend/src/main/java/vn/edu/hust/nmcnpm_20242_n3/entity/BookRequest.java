package vn.edu.hust.nmcnpm_20242_n3.entity;

import vn.edu.hust.nmcnpm_20242_n3.constant.BookRequestStatusEnum;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookRequestTypeEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

// Needed for HTTP requests and responses
@Setter
@Getter

@Entity
@Table(name = "book_requests")
public class BookRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne(cascade = CascadeType.ALL)
    BookLoan bookLoan;

    @Enumerated(EnumType.STRING)
    BookRequestStatusEnum status;

    @Enumerated(EnumType.STRING)
    BookRequestTypeEnum type;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    Date CreatedAt;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    Date UpdatedAt;

    @PrePersist
    protected void onCreate() {
        CreatedAt = new Date();
        UpdatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        UpdatedAt = new Date();
    }
}
