package vn.edu.hust.nmcnpm_20242_n3.controller;

import java.util.List;


import lombok.NoArgsConstructor;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookCopyStatusEnum;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookCopy;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookLoan;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookRequest;
import vn.edu.hust.nmcnpm_20242_n3.repository.BookCopyRepository;
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
import org.springframework.web.bind.annotation.PathVariable;
import vn.edu.hust.nmcnpm_20242_n3.service.BookRequestService;

@RestController
@RequestMapping("/api/loaned")
@NoArgsConstructor
public class BookLoanController {

    private BookLoanService bookLoanService;


    @Autowired
    public BookLoanController(BookLoanService bookLoanService, BookRequestService bookRequestService) {
        this.bookLoanService = bookLoanService;
    }

    @GetMapping("/{userId}")
    public List<BookLoan> getAllLoans(@PathVariable("userId") String userId) {
        return bookLoanService.getAllLoansByUserId(userId);
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
