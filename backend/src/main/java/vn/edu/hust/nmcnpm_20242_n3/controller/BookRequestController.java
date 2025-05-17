package vn.edu.hust.nmcnpm_20242_n3.controller;

import java.util.List;
import vn.edu.hust.nmcnpm_20242_n3.service.BookRequestService;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookCopyStatusEnum;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookRequestTypeEnum;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookRequest;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookCopy;

import vn.edu.hust.nmcnpm_20242_n3.repository.BookCopyRepository;

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
@RequestMapping("/api/requests")
public class BookRequestController {
    private final BookRequestService bookRequestService;
    private final BookCopyRepository bookCopyRepository;

    @Autowired
    public BookRequestController(BookRequestService bookRequestService, BookCopyRepository bookCopyRepository) {
        this.bookRequestService = bookRequestService;
        this.bookCopyRepository = bookCopyRepository;
    }

    @GetMapping("/{userId}")
    public List<BookRequest> listAllRequestsFromUser(@PathVariable("userId") String userId) {
        return bookRequestService.listAllRequestsFromUser(userId);
    }

    @PostMapping("/{userId}/new/borrow")
    public ResponseEntity<?> newBorrowingRequest(@PathVariable("userId") String userId,
            @RequestParam("bookCopyId") Integer bookCopyId) {
        try {
            return new ResponseEntity<>(bookRequestService.newBorrowingRequest(userId, bookCopyId),
                    HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{userId}/new/borrow/rand")
    public ResponseEntity<?> newBorrowingRequest_Random(@PathVariable("userId") String userId,
            @RequestParam("bookId") Integer bookId) {
        try {
            BookCopy bookCopy = bookCopyRepository
                    .findFirstByOriginalBook_BookIdAndStatus(bookId, BookCopyStatusEnum.AVAILABLE)
                    .orElseThrow(() -> new IllegalArgumentException("No such book copy found!"));
            Integer bookCopyId = bookCopy.getId();

            return new ResponseEntity<>(bookRequestService.newBorrowingRequest(userId, bookCopyId), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{userId}/new/return")
    public ResponseEntity<?> newReturningRequest(@PathVariable("userId") String userId,
            @RequestParam("bookCopyId") Integer bookCopyId) {
        try {
            return new ResponseEntity<>(bookRequestService.newReturningRequest(userId, bookCopyId), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{userId}/cancel")
    public ResponseEntity<?> cancelRequest(@PathVariable("userId") String userId,
            @RequestParam("requestId") String requestId) {

        try {
            BookRequest bookRequest = bookRequestService.findRequestById(requestId);
            if (!bookRequest.getUser().getId().equals(userId)) {
                return new ResponseEntity<>("You are not allowed to cancel this request", HttpStatus.FORBIDDEN);
            }
            bookRequestService.cancelRequest(requestId);
            return new ResponseEntity<>("Request cancelled successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
