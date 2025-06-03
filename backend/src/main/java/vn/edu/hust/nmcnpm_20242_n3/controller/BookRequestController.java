package vn.edu.hust.nmcnpm_20242_n3.controller;

import java.util.List;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookCopyStatusEnum;
import vn.edu.hust.nmcnpm_20242_n3.dto.BookRequestDTO;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookCopy;
import vn.edu.hust.nmcnpm_20242_n3.repository.BookCopyRepository;
import vn.edu.hust.nmcnpm_20242_n3.service.BookRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/requests"})
public class BookRequestController {

    private final BookRequestService bookRequestService;
    private final BookCopyRepository bookCopyRepository;

    @Autowired
    public BookRequestController(BookRequestService bookRequestService, BookCopyRepository bookCopyRepository) {
        this.bookRequestService = bookRequestService;
        this.bookCopyRepository = bookCopyRepository;
    }

    @GetMapping
    public ResponseEntity<?> getAllRequests() {
        try {
            List<BookRequestDTO> requests = bookRequestService.getAllRequests();
            return ResponseEntity.ok().body(requests);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/process/{requestId}/{approve}")
    public ResponseEntity<?> processRequest(@PathVariable String requestId, @PathVariable boolean approve) {
        try {
            BookRequestDTO updatedRequest = bookRequestService.processRequest(requestId, approve);
            String message = approve ? "Request approved successfully" : "Request rejected successfully";
            return ResponseEntity.ok().body(new ResponseMessage(updatedRequest, message));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public List<BookRequestDTO> listAllRequestsFromUser(@PathVariable("userId") String userId) {
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

    @PutMapping("/cancel")
    public ResponseEntity<?> cancelRequest(@RequestParam("requestId") String requestId) {
        try {
            bookRequestService.cancelRequest(requestId);
            return new ResponseEntity<>("Request cancelled successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    static class ResponseMessage {
        private BookRequestDTO request;
        private String message;

        public ResponseMessage(BookRequestDTO request, String message) {
            this.request = request;
            this.message = message;
        }

        public BookRequestDTO getRequest() { return request; }
        public String getMessage() { return message; }
    }
}