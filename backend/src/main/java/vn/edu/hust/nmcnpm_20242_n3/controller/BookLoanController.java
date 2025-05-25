package vn.edu.hust.nmcnpm_20242_n3.controller;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookCopyStatusEnum;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookLoanStatusEnum;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookCopy;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookLoan;
import vn.edu.hust.nmcnpm_20242_n3.entity.User;
import vn.edu.hust.nmcnpm_20242_n3.service.BookLoanService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/loaned")
@NoArgsConstructor
public class BookLoanController {

    private BookLoanService bookLoanService;

    @Autowired
    public BookLoanController(BookLoanService bookLoanService) {
        this.bookLoanService = bookLoanService;
    }

    @GetMapping("/{userId}")
    public List<BookLoan> getAllLoans(@PathVariable("userId") String userId) {
        return bookLoanService.getAllLoansByUserId(userId);
    }

    @PostMapping("/add/user/{bookCopyId}/{userId}")
    public boolean addUserToBookLoanList(@PathVariable int bookCopyId,@PathVariable String userId) {
        BookCopy bookCopy = bookLoanService.getBookCopyById(bookCopyId);
        User user = bookLoanService.getUserById(userId);

        if (bookCopy == null || user == null || bookCopy.getStatus() != BookCopyStatusEnum.AVAILABLE) {
            throw new IllegalArgumentException("BookCopy and User cannot be null");
        }

            BookLoan bookLoan = new BookLoan();
            bookLoan.setBookCopy(bookCopy);
            bookLoan.setUser(user);
            bookLoan.setLoan_duration(30); // Default loan duration
            bookLoan.setStatus(BookLoanStatusEnum.BORROWED);
            bookLoan.setLoanedAt(new Date());
        // Set the book copy status to UNAVAILABLE
            bookCopy.setStatus(BookCopyStatusEnum.UNAVAILABLE);
        try {
            bookLoanService.save(bookLoan);
        } catch (Exception e){
            return false;
        }
        return true;
    }

    @GetMapping("/borrowed-book/{userId}")
    public ResponseEntity<?> getBorrowedBooksByUserId(@PathVariable String userId) {
        try {
            var borrowedBooks = bookLoanService.getBorrowedBooksByUserId(userId);
            return new ResponseEntity<>(borrowedBooks, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/return/{bookLoanId}")
    public ResponseEntity<?> returnBook(@PathVariable String bookLoanId) {
        try {
            BookLoan bookLoan = bookLoanService.getBookLoanById(bookLoanId);
            if (bookLoan == null || bookLoan.getStatus() != BookLoanStatusEnum.BORROWED) {
                return new ResponseEntity<>("Book loan not found or not in BORROWED state", HttpStatus.NOT_FOUND);
            }
            bookLoan.setStatus(BookLoanStatusEnum.RETURNED);
            bookLoan.setActualReturnDate(new Date());
            bookLoanService.save(bookLoan);

            // Update the book copy status to AVAILABLE
            BookCopy bookCopy = bookLoan.getBookCopy();
            if (bookCopy != null) {
                bookCopy.setStatus(BookCopyStatusEnum.AVAILABLE);
                bookLoanService.saveBookCopy(bookCopy);
            }

            return new ResponseEntity<>("Book returned successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/extend/{borrowId}")
    public ResponseEntity<?> extendBorrowPeriod(@PathVariable String borrowId, @RequestParam Date newReturnDate) {
        try {
            BookLoan bookLoan = bookLoanService.getBookLoanById(borrowId);
            if (bookLoan == null || bookLoan.getStatus() != BookLoanStatusEnum.BORROWED) {
                return new ResponseEntity<>("Book loan not found or not in BORROWED state", HttpStatus.NOT_FOUND);
            }

            if (newReturnDate.before(bookLoan.getDueDate())) {
                return new ResponseEntity<>("New return date must be after the current due date", HttpStatus.BAD_REQUEST);
            }

            bookLoan.setDueDate(newReturnDate);
            bookLoanService.save(bookLoan);

            return new ResponseEntity<>("Borrow period extended successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/history/user/{userId}")
    public ResponseEntity<?> getBorrowingHistory(@PathVariable String userId) {
        try {
            List<BookLoan> history = bookLoanService.getAllLoansByUserId(userId);
            return new ResponseEntity<>(history, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/history/book-copy/{bookCopyId}")
    public ResponseEntity<?> getBorrowHistoryByBookCopyId(@PathVariable int bookCopyId) {
        try {
            List<BookLoan> history = bookLoanService.getBorrowHistoryByBookCopyId(bookCopyId);
            return new ResponseEntity<>(history, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/overdue")
    public ResponseEntity<?> getOverdueLoans() {
        try {
            List<BookLoan> overdueLoans = bookLoanService.getOverdueLoans();
            return new ResponseEntity<>(overdueLoans, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
