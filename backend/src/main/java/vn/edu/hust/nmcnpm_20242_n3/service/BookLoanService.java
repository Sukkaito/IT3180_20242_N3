package vn.edu.hust.nmcnpm_20242_n3.service;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookCopyStatusEnum;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookLoanStatusEnum;
import vn.edu.hust.nmcnpm_20242_n3.constant.UserStatusEnum;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookCopy;
import vn.edu.hust.nmcnpm_20242_n3.entity.User;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookLoan;
import vn.edu.hust.nmcnpm_20242_n3.repository.BookCopyRepository;
import vn.edu.hust.nmcnpm_20242_n3.repository.BookLoanRepository;
import vn.edu.hust.nmcnpm_20242_n3.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@Service
public class BookLoanService {

    private final BookCopyRepository bookCopyRepository;
    private final BookLoanRepository bookLoanRepository;
    private final UserRepository userRepository;

    @Autowired
    public BookLoanService(BookCopyRepository bookCopyRepository, BookLoanRepository bookLoanRepository, UserRepository userRepository) {
        this.bookCopyRepository = bookCopyRepository;
        this.bookLoanRepository = bookLoanRepository;
        this.userRepository = userRepository;
    }
    public List<BookLoan> getAllLoans() {
        return (List<BookLoan>) bookLoanRepository.findAll();
    }
    @Transactional
    public BookLoan newBookLoan(String userId, String bookCopyId) {
        // Check if user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        BookCopy bookCopy = bookCopyRepository.findById(bookCopyId)
                .orElseThrow(() -> new IllegalArgumentException("Book copy not found"));
        if (bookCopy.getStatus() != BookCopyStatusEnum.AVAILABLE) {
            throw new IllegalArgumentException("Book copy is not available");
        }

        // Create a new book loan
        BookLoan bookLoan = new BookLoan();
        bookLoan.setUser(user);
        bookLoan.setBookCopy(bookCopy);
        bookLoan.setStatus(BookLoanStatusEnum.BORROWED);
        bookLoan.setLoanedAt(new Date());
        
        bookCopyRepository.save(bookCopy);

        return bookLoanRepository.save(bookLoan);
    }
    @Transactional
    public BookLoan changeLoanDuration(String bookLoanId, int newDuration) {
        BookLoan bookLoan = bookLoanRepository.findById(bookLoanId)
                .orElseThrow(() -> new IllegalArgumentException("Book loan not found"));
        bookLoan.setLoan_duration(newDuration);
        //check if the new due date is before the current date
        if (bookLoan.getDueDate() != null && bookLoan.getDueDate().before(new Date())) {
            bookLoan.setStatus(BookLoanStatusEnum.OVERDUE);
        }
        return bookLoanRepository.save(bookLoan);
    }
    @Scheduled(cron = "0 0 4 * * ?") // Every day at 4 AM
    @Transactional
    public void checkOverdueBookLoans() {
        List<BookLoan> borrowedLoans = bookLoanRepository.findByStatus(BookLoanStatusEnum.BORROWED);
        Date currentDate = new Date();

        for (BookLoan loan : borrowedLoans) {
            if (loan.getDueDate() != null && loan.getDueDate().before(currentDate)) {
                loan.setStatus(BookLoanStatusEnum.OVERDUE);
                User user = loan.getUser();
                user.setStatus(UserStatusEnum.BLOCKED);
                bookLoanRepository.save(loan);
            }
        }

    }
    @Transactional
    public void ReturningBook(String bookCopyId){
        BookLoan bookLoan = bookLoanRepository.findByBookCopyId(bookCopyId)
                .orElseThrow(() -> new IllegalArgumentException("Book loan not found"));
        BookCopy bookCopy = bookLoan.getBookCopy();
        bookCopy.setStatus(BookCopyStatusEnum.AVAILABLE);
        bookCopyRepository.save(bookCopy);
        bookLoan.setStatus(BookLoanStatusEnum.RETURNED);
        bookLoan.setActualReturnDate(new Date());
        bookLoanRepository.save(bookLoan);
    }
}