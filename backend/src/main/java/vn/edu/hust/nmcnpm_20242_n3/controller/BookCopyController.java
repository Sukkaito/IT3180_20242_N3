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

    @PutMapping("/browsing/{userId}/{bookCopyId}")
    public ResponseEntity<?> browsingBookCopy(@RequestParam String bookCopyId ,
                                              @RequestParam String userId) {
        try {
            var bookCopy = bookCopyService.getBookCopyById(bookCopyId);
            if (bookCopy == null) {
                return ResponseEntity.status(404).body("Book copy not found");
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing the request: " + e.getMessage());
        }
        return null;
    }

//    @PutMapping

    
}
