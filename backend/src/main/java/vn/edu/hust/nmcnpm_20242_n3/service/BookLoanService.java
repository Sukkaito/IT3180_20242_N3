package vn.edu.hust.nmcnpm_20242_n3.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookCopyStatusEnum;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookLoanStatusEnum;
import vn.edu.hust.nmcnpm_20242_n3.entity.*;
import vn.edu.hust.nmcnpm_20242_n3.repository.BookCopyRepository;
import vn.edu.hust.nmcnpm_20242_n3.repository.BookLoanRepository;
import vn.edu.hust.nmcnpm_20242_n3.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@Service
@RequiredArgsConstructor
public class BookLoanService {

    private  BookCopyRepository bookCopyRepository;
    private  BookLoanRepository bookLoanRepository;
    private  UserRepository userRepository;

    @Autowired
    public BookLoanService(BookCopyRepository bookCopyRepository, BookLoanRepository bookLoanRepository,
                           UserRepository userRepository) {
        this.bookCopyRepository = bookCopyRepository;
        this.bookLoanRepository = bookLoanRepository;

        this.userRepository = userRepository;
    }


    public Optional<BookLoan> findBookLoanByBookCopyId(String bookCopyId) {
        return bookLoanRepository.findByBookCopyId(bookCopyId);
    }


    public List<BookLoan> getAllLoansByUserId(String userId) {
        return (List<BookLoan>) bookLoanRepository.findAllByUserId(userId);
    }


    @Transactional
    public BookLoan newBookLoan(String userId, String bookCopyId) {
        // Check if user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        BookCopy bookCopy = bookCopyRepository.findById(bookCopyId)
                .orElseThrow(() -> new IllegalArgumentException("Book copy not found"));
        if (!bookCopy.getStatus().equals(BookCopyStatusEnum.AVAILABLE)) {
            throw new IllegalArgumentException("Book copy is not available");
        }
        // Create a new book loan
        BookLoan bookLoan = new BookLoan();
        bookLoan.setUser(user);
        bookLoan.setBookCopy(bookCopy);
        bookLoan.setLoan_duration(30);
        bookLoan.setStatus(BookLoanStatusEnum.BORROWED);
        bookLoan.setLoanedAt(new Date());

        bookCopyRepository.save(bookCopy);
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
                bookLoanRepository.save(loan);
            }
        }
    }

    public Optional<BookLoan> findBookLoanByBookCopyIdAndUserIdAndStatus(String bookCopyId, String userId,
            BookLoanStatusEnum status) {
        return bookLoanRepository.findByBookCopyIdAndUserIdAndStatus(userId, bookCopyId, status);
    }

    public List<Book> getBorrowedBooksByUserId(String userId) {
        return bookLoanRepository.findBorrowedBooksByUserId(userId);
    }

    public List<User> getUserListByBookCopyId(String bookCopyId) {
        return bookLoanRepository.getUserListByBookCopyId(bookCopyId);
    }


    public void removeUserFromBookLoanList(String userId) {
        bookLoanRepository.deleteByUserId(userId);
    }

    public boolean addUserToBookLoanList(BookCopy bookCopy,User user) {
        BookLoan bookLoan = new BookLoan(bookCopy ,user);
        return bookLoanRepository.save(bookLoan) != null;
    }


}