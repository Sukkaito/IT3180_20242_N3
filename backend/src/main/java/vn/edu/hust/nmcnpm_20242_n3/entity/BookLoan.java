package vn.edu.hust.nmcnpm_20242_n3.entity;

import vn.edu.hust.nmcnpm_20242_n3.constant.BookLoanStatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Setter
@Getter

@Entity
@Table(name = "book_loans")
public class BookLoan {
    // define default loan duration to be 30 days
    @Column(name = "loan_duration", nullable = false)
    public int loan_duration = 30;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

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

    Date DueDate;

    @PrePersist
    protected void onCreate() {
        LoanedAt = new Date();
        UpdatedAt = new Date();
        loan_duration = 30; // Set default loan duration to 30 days

    }

    @PreUpdate
    protected void onUpdate() {
        UpdatedAt = new Date();
        DueDate = new Date(LoanedAt.getTime() + (loan_duration * 24 * 60 * 60 * 1000)); // Set due date to loan duration
    }
}
