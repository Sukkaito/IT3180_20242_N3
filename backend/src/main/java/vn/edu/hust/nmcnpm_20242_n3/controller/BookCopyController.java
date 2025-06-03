package vn.edu.hust.nmcnpm_20242_n3.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookCopyStatusEnum;
import vn.edu.hust.nmcnpm_20242_n3.dto.BookCopyDTO;
import vn.edu.hust.nmcnpm_20242_n3.service.BookCopyService;

import java.util.List;

@RestController
@RequestMapping("/api/book-copy")
public class BookCopyController {

    @Autowired
    private BookCopyService bookCopyService;

    @GetMapping("/available")
    public ResponseEntity<?> getAllAvailableBookCopies() {
        try {
            List<BookCopyDTO> availableBookCopies = bookCopyService.getAllAvailableBookCopies();
            return ResponseEntity.ok(availableBookCopies);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving available book copies: " + e.getMessage());
        }
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<?> getBookCopiesByBookId(@PathVariable Integer bookId) {
        try {
            List<BookCopyDTO> bookCopies = bookCopyService.getBookCopiesByBookId(bookId);
            return ResponseEntity.ok(bookCopies);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving available book copies: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createBookCopy(@RequestBody BookCopyDTO bookCopyDTO) {
        try {
            BookCopyDTO createdBookCopy = bookCopyService.createBookCopy(bookCopyDTO);
            return ResponseEntity.status(201).body(createdBookCopy);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating book copy: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBookCopy(@PathVariable int id) {
        try {
            bookCopyService.deleteBookCopy(id);
            return ResponseEntity.ok("Book copy deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting book copy: " + e.getMessage());
        }
    }
}
