package vn.edu.hust.nmcnpm_20242_n3.entity;

import vn.edu.hust.nmcnpm_20242_n3.constant.BookLoanStatusEnum;
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
@Table(name = "book_loans")
public class BookLoan {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne(cascade = CascadeType.ALL)
    BookCopy bookCopy;

    @ManyToOne(cascade = CascadeType.ALL)
    User user;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    Date loanDate;

    @Temporal(TemporalType.TIMESTAMP)
    Date returnDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
    Date actualReturnDate;

    @Enumerated(EnumType.STRING)
    BookLoanStatusEnum status;

    @Column(nullable = true)
    String currentBookRequestId;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    Date LoanedAt;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    Date UpdatedAt;

    @PrePersist
    protected void onCreate() {
        LoanedAt = new Date();
        UpdatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        UpdatedAt = new Date();
    }
}

