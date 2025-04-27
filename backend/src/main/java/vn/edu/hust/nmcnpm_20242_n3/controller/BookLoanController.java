package vn.edu.hust.nmcnpm_20242_n3.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.edu.hust.nmcnpm_20242_n3.service.BookLoanService;

@RestController
@RequestMapping("/api/bookloans")
public class BookLoanController {

    private BookLoanService bookLoanService;

    @Autowired
    public BookLoanController(BookLoanService bookLoanService) {
        this.bookLoanService = bookLoanService;
    }

    // Endpoint to borrow a book
    @PostMapping("/borrow")
    public ResponseEntity<?> borrowBook(@RequestParam String userId, @RequestParam Integer bookId) {
        try {
            return new ResponseEntity<>(bookLoanService.borrowBook(userId, bookId), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Endpoint to return a book
    @PostMapping("/return")
    public ResponseEntity<?> returnBook(@RequestParam String bookLoanId) {
        try {
            bookLoanService.Returning(bookLoanId);
            return new ResponseEntity<>("Book returned successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
