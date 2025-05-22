package vn.edu.hust.nmcnpm_20242_n3.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookCopyStatusEnum;
import vn.edu.hust.nmcnpm_20242_n3.service.BookCopyService;

@RestController
@RequestMapping("/api/book-copy")
public class BookCopyController {

    @Autowired
    private BookCopyService bookCopyService;

    @GetMapping
    public ResponseEntity<?> getAllAvailableBookCopies() {
        try {
            var availableBookCopies = bookCopyService.getAllAvailableBookCopies();
            return ResponseEntity.ok(availableBookCopies);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving available book copies: " + e.getMessage());
        }
    }

    @PutMapping("/browsing/{userId}/{bookCopyId}")
    public ResponseEntity<?> browsingBookCopy(@PathVariable int bookCopyId ,
                                              @PathVariable String userId) {
        try {
            var bookCopy = bookCopyService.getBookCopyById(bookCopyId);
            if (bookCopy == null) {
                return ResponseEntity.status(404).body("Book copy not found");
            }

            if ( !bookCopyService.isAvailable(bookCopyId)) {
                return ResponseEntity.status(400).body("Book copy is not available for browsing");
            }

            // Update the status of the book copy
            bookCopyService.setStatus(bookCopyId, BookCopyStatusEnum.UNAVAILABLE);

            return ResponseEntity.ok("Book copy is now being browsed by user " + userId);


        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing the request: " + e.getMessage());
        }
    }

//    @PutMapping

    
}
