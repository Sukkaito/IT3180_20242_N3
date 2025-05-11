package vn.edu.hust.nmcnpm_20242_n3.controller;

import java.util.List;
import vn.edu.hust.nmcnpm_20242_n3.service.BookRequestService;

import vn.edu.hust.nmcnpm_20242_n3.entity.BookRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/requests")
public class BookRequestController {
    private final BookRequestService bookRequestService;

    @Autowired
    public BookRequestController(BookRequestService bookRequestService) {
        this.bookRequestService = bookRequestService;
    }
    @GetMapping("/")
    public List<BookRequest> getAllRequests() {
        return bookRequestService.getAllRequests();
    }
    
    @PostMapping("/b/new")
    public ResponseEntity<?> borrowBook(@RequestParam String userId, @RequestParam Integer bookId) {
        try {
            return new ResponseEntity<>(bookRequestService.newBorrowingRequest(userId, bookId), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/b/{requestId}/accept")
    public ResponseEntity<?> acceptBorrowingRequest(@PathVariable String requestId) {
        try {
            return new ResponseEntity<>(bookRequestService.acceptBorrowingRequest(requestId), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/b/{requestId}/reject")
    public ResponseEntity<?> rejectBorrowingRequest(@PathVariable String requestId) {
        try {
            return new ResponseEntity<>(bookRequestService.rejectBorrowingRequest(requestId), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/b/{requestId}/cancel")
    public ResponseEntity<?> cancelBorrowingRequest(@PathVariable String requestId) {
        try {
            bookRequestService.cancelBorrowingRequest(requestId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/r/new")
    public ResponseEntity<?> returnBook(@RequestParam String userId, @RequestParam String bookCopyId) {
        try {
            return new ResponseEntity<>(bookRequestService.newReturningRequest(userId, bookCopyId), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/r/{requestId}/accept")
    public ResponseEntity<?> acceptReeturninggRequest(@PathVariable String requestId) {
        try {
            return new ResponseEntity<>(bookRequestService.acceptReturningRequest(requestId), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/r/{requestId}/reject")
    public ResponseEntity<?> rejectReturningRequest(@PathVariable String requestId) {
        try {
            return new ResponseEntity<>(bookRequestService.rejectReturningRequest(requestId), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/r/{requestId}/cancel")
    public ResponseEntity<?> cancelReturningRequest(@PathVariable String requestId) {
        try {
            bookRequestService.cancelReturningRequest(requestId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
