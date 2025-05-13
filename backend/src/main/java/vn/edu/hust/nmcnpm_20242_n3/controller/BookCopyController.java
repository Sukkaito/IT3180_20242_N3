package vn.edu.hust.nmcnpm_20242_n3.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @PutMapping("/browsiing/{userId}/{bookCopyId}")
    public ResponseEntity<?> browsingBookCopy(@RequestParam String bookCopyId) {
        try {
            var bookCopy = bookCopyService.getBookCopyById(bookCopyId);
            if (bookCopy == null) {
                return ResponseEntity.status(404).body("Book copy not found");
            }
            if ("unavailable".equalsIgnoreCase(bookCopy.getStattus(bookCopyId))) {
                return ResponseEntity.status(400).body("The book copy is not available for borrowing");
            } else if ("available".equalsIgnoreCase(bookCopy.getStattus(bookCopyId))) {
                return ResponseEntity.ok("Borrowing successful. You can come to pick up the book");
            } else {
                return ResponseEntity.status(400).body("Invalid book copy status");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing the request: " + e.getMessage());
        }
    }

    
}
