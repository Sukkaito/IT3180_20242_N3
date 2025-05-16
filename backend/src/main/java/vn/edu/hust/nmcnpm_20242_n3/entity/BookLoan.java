package vn.edu.hust.nmcnpm_20242_n3.entity;

import vn.edu.hust.nmcnpm_20242_n3.constant.BookLoanStatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Setter
@Getter

@Entity
@Table(name = "book_loans")
public class BookLoan {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(cascade = CascadeType.ALL)
    private BookCopy bookCopy;

    @ManyToOne(cascade = CascadeType.ALL)
     private User user;

    @CreationTimestamp
    @Column(name = "loan_date")
    private LocalDateTime loanDate;

    @Column(name = "return_date")
    private LocalDateTime returnDate;

    @Column(name = "actual_return_date", nullable = true)
    private LocalDateTime actualReturnDate;

    @Enumerated(EnumType.STRING)
    private BookLoanStatusEnum status;

    @Column(name = "current_book_request_id", nullable = true)
    private String currentBookRequestId;

    @CreationTimestamp
    @Column(name = "loaned_at")
    private LocalDateTime loanedAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        loanedAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}