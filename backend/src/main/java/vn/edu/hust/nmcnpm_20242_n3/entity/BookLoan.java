package vn.edu.hust.nmcnpm_20242_n3.entity;

import vn.edu.hust.nmcnpm_20242_n3.constant.BookLoanStatusEnum;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

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
    @Temporal(TemporalType.TIMESTAMP)
    private Date loanDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date returnDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
    private Date actualReturnDate;

    @Enumerated(EnumType.STRING)
    private BookLoanStatusEnum status;

    @Column(nullable = true)
    private String currentBookRequestId;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date LoanedAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date UpdatedAt;

    @PrePersist
    protected void onCreate() {
        LoanedAt = new Date();
        UpdatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        UpdatedAt = new Date();
    }

    // Getter and Setter methods
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BookCopy getBookCopy() {
        return bookCopy;
    }

    public void setBookCopy(BookCopy bookCopy) {
        this.bookCopy = bookCopy;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(Date loanDate) {
        this.loanDate = loanDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public Date getActualReturnDate() {
        return actualReturnDate;
    }

    public void setActualReturnDate(Date actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
    }

    public BookLoanStatusEnum getStatus() {
        return status;
    }

    public void setStatus(BookLoanStatusEnum status) {
        this.status = status;
    }

    public String getCurrentBookRequestId() {
        return currentBookRequestId;
    }

    public void setCurrentBookRequestId(String currentBookRequestId) {
        this.currentBookRequestId = currentBookRequestId;
    }

    public Date getLoanedAt() {
        return LoanedAt;
    }

    public void setLoanedAt(Date loanedAt) {
        LoanedAt = loanedAt;
    }

    public Date getUpdatedAt() {
        return UpdatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        UpdatedAt = updatedAt;
    }
}
