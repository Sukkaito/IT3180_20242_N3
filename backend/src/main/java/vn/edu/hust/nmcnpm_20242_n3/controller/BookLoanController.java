package vn.edu.hust.nmcnpm_20242_n3.controller;

import java.util.List;


import lombok.NoArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import vn.edu.hust.nmcnpm_20242_n3.dto.BookLoanDTO;
import vn.edu.hust.nmcnpm_20242_n3.service.AuthenticationService;
import vn.edu.hust.nmcnpm_20242_n3.service.BookLoanService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/loaned")
@NoArgsConstructor
public class BookLoanController {

    private BookLoanService bookLoanService;
    private AuthenticationService authenticationService;


    @Autowired
    public BookLoanController(BookLoanService bookLoanService, AuthenticationService authenticationService) {
        this.bookLoanService = bookLoanService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_STAFF') or @authenticationService.isAuthorizedUser(#userId)")
    public List<BookLoanDTO> getAllLoans(@PathVariable("userId") String userId) {
        return bookLoanService.getAllLoansByUserId(userId);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_STAFF')")
    public List<BookLoanDTO> getAllLoans() {
        return bookLoanService.getAllBookLoans();
    }

    @GetMapping("/history/book-copy/{bookCopyId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_STAFF')")
    public ResponseEntity<?> getBorrowHistoryByBookCopyId(@PathVariable int bookCopyId) {
        try {
            List<BookLoanDTO> history = bookLoanService.getBorrowHistoryByBookCopyId(bookCopyId);
            return new ResponseEntity<>(history, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/overdue")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_STAFF')")
    public ResponseEntity<?> getOverdueLoans() {
        try {
            List<BookLoanDTO> overdueLoans = bookLoanService.getOverdueLoans();
            return new ResponseEntity<>(overdueLoans, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
