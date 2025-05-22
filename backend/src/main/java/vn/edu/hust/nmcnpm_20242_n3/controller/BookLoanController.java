package vn.edu.hust.nmcnpm_20242_n3.controller;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
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

    @PostMapping("/add-to-loan-queue/{bookCopyId}/{userId}")
    public ResponseEntity<?> addUserToBookLoanQueue(@RequestBody BookCopy bookCopy, @RequestBody User user) {
        try {
            var added = bookLoanService.addUserToBookLoanList(bookCopy, user);
            if (added) {
                return new ResponseEntity<>("User added to the loan queue successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Book copy is not unavailable", HttpStatus.BAD_REQUEST);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/borrowed-books-by-user-id/{userId}")
    public ResponseEntity<?> getBorrowedBooksByUserId(@PathVariable String userId) {
        try {
            var borrowedBooks = bookLoanService.getBorrowedBooksByUserId(userId);
            return new ResponseEntity<>(borrowedBooks, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users-by-book-copy-id/{bookCopyId}")
    public ResponseEntity<?> getUserListByBookCopyId(@PathVariable int bookCopyId) {
        try {
            var users = bookLoanService.getUserListByBookCopyId(bookCopyId);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
