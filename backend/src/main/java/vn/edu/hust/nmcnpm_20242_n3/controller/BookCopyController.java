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

    @GetMapping("/available")
    public ResponseEntity<?> getAllAvailableBookCopies() {
        try {
            var availableBookCopies = bookCopyService.getAllAvailableBookCopies();
            return ResponseEntity.ok(availableBookCopies);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving available book copies: " + e.getMessage());
        }
    }

    @GetMapping("/book-copy/{bookId}")
    public ResponseEntity<?> getBookCopiesByBookId(@PathVariable Integer bookId) {
        try {
            var bookCopies = bookCopyService.getBookCopiesByBookId(bookId);
            return ResponseEntity.ok(bookCopies);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving available book copies: " + e.getMessage());
        }
    }

    
}
