package vn.edu.hust.nmcnpm_20242_n3.controller;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookCopy;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookLoan;
import vn.edu.hust.nmcnpm_20242_n3.entity.User;
import vn.edu.hust.nmcnpm_20242_n3.service.BookLoanService;


@RestController
@RequestMapping("/api/book-loan")
@AllArgsConstructor
@NoArgsConstructor
public class BookLoanController {
    @Autowired
    private BookLoanService bookLoanService;

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

    @GetMapping("/users-by-book-copy-id/{bookCopyId}")
    public ResponseEntity<?> getUserListByBookCopyId(@PathVariable String bookCopyId) {
        try {
            var users = bookLoanService.getUserListByBookCopyId(bookCopyId);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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



}
