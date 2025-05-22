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
    private BookCopy bookCopy;

    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

    @CreationTimestamp
    @Column(name = "loan_date")
    private Date loanDate;

    @Column(name = "return_date")
    private Date returnDate;

    @Column(name = "actual_return_date", nullable = true)
    private Date actualReturnDate;

    @Enumerated(EnumType.STRING)
    private BookLoanStatusEnum status;

    @Column(name = "current_book_request_id", nullable = true)
    private String currentBookRequestId;

    @CreationTimestamp
    @Column(name = "loaned_at")
    private Date LoanedAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date UpdatedAt;

    private Date DueDate;

    @PrePersist
    protected void onCreate() {
        LoanedAt = new Date();
        UpdatedAt = new Date();
        loan_duration = 30; // Set default loan duration to 30 days

    }

    @PreUpdate
    protected void onUpdate() {
        UpdatedAt = new Date();
        if (LoanedAt != null) {
            DueDate = new Date(LoanedAt.getTime() + (loan_duration * 24 * 60 * 60 * 1000)); // Set due date to loan duration
        }
    }

    public BookLoan() {
    }
    public BookLoan(BookCopy bookcopy, User user) {
        this.bookCopy = bookcopy;
        this.user = user;
        this.status = BookLoanStatusEnum.REQUEST_BORROWING;
    }

    public BookLoan(BookCopy bookcopy, User user, BookLoanStatusEnum status) {
        this.bookCopy = bookcopy;
        this.user = user;
        this.status = status;
    }


}