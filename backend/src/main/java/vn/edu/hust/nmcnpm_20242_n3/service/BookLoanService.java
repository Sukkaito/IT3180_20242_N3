package vn.edu.hust.nmcnpm_20242_n3.service;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookCopyStatusEnum;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookLoanStatusEnum;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookCopy;
import vn.edu.hust.nmcnpm_20242_n3.entity.User;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookLoan;
import vn.edu.hust.nmcnpm_20242_n3.repository.BookCopyRepository;
import vn.edu.hust.nmcnpm_20242_n3.repository.BookLoanRepository;
import vn.edu.hust.nmcnpm_20242_n3.repository.UserRepository;

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

    @Transactional
    public BookLoan borrowBook(String userId, Integer bookId) {
        // Check if user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Find an available copy of the book
        BookCopy bookCopy = bookCopyRepository.findFirstByOriginalBook_BookIdAndStatus(bookId, BookCopyStatusEnum.AVAILABLE)
                .orElseThrow(() -> new IllegalArgumentException("No available copies for this book"));

        // Create a new book loan
        BookLoan bookLoan = new BookLoan();
        bookLoan.setBookCopy(bookCopy);
        bookLoan.setUser(user);
        bookLoan.setLoanDate(new Date());
        bookLoan.setStatus(BookLoanStatusEnum.BORROWED);

        // Update book copy status
        bookCopy.setStatus(BookCopyStatusEnum.UNAVAILABLE);
        bookCopyRepository.save(bookCopy);

        return bookLoanRepository.save(bookLoan);
    }
    public void Returning(String bookLoanId){
        BookLoan bookLoan = bookLoanRepository.findById(bookLoanId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        BookCopy bookCopy = bookLoan.getBookCopy();
        bookLoan.setStatus(BookLoanStatusEnum.RETURNED);
        bookCopy.setStatus(BookCopyStatusEnum.AVAILABLE);
        bookLoanRepository.save(bookLoan);
        bookCopyRepository.save(bookCopy);
    }
}