package vn.edu.hust.nmcnpm_20242_n3.controller;

import java.util.List;
import vn.edu.hust.nmcnpm_20242_n3.service.BookRequestService;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookRequestTypeEnum;
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
@RequestMapping("/api/requests")
public class BookRequestController {
    private final BookRequestService bookRequestService;

    @Autowired
    public BookRequestController(BookRequestService bookRequestService) {
        this.bookRequestService = bookRequestService;
    }
    @GetMapping("/{userId}")
    public List<BookRequest> listAllRequestsFromUser(@PathVariable String userId) {
        return bookRequestService.listAllRequestsFromUser(userId);
    }
    @PostMapping("/{userId}/new_rq/type=borrow")
    public ResponseEntity<?> newBorrowingRequest(@RequestParam String userId, @RequestParam String bookCopyId) {
        try {
            return new ResponseEntity<>(bookRequestService.newBorrowingRequest(userId, bookCopyId), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/{userId}/new_rq/type=return")
    public ResponseEntity<?> newReturningRequest(@PathVariable String userId, @RequestParam String bookCopyId) {
        try {
            return new ResponseEntity<>(bookRequestService.newReturningRequest(userId, bookCopyId), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/{userId}/{requestId}/cancel")
    public ResponseEntity<?> cancelRequest(@PathVariable String userId, @PathVariable String requestId) {
        BookRequest bookRequest = bookRequestService.findRequestById(requestId);
        if(bookRequest.getUser().getId() != userId){
            return new ResponseEntity<>("You are not allowed to cancel this request", HttpStatus.FORBIDDEN);
        } 
        try {
            if (bookRequest.getType() == BookRequestTypeEnum.BORROWING)
                return new ResponseEntity<>(bookRequestService.cancelBorrowingRequest(requestId), HttpStatus.NO_CONTENT);
            else if(bookRequest.getType() == BookRequestTypeEnum.RETURNING) 
                return new ResponseEntity<>(bookRequestService.cancelReturningRequest(requestId), HttpStatus.NO_CONTENT);
            else return new ResponseEntity<>("Invalid request type", HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
