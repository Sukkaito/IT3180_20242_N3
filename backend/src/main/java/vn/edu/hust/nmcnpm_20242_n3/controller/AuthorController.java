package vn.edu.hust.nmcnpm_20242_n3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hust.nmcnpm_20242_n3.entity.Author;
import vn.edu.hust.nmcnpm_20242_n3.service.AuthorService;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public ResponseEntity<?> getAllAuthors() {
        try {
            List<Author> authors = authorService.getAllAuthors();
            return ResponseEntity.ok(authors);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving authors: " + e.getMessage());
        }
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<?> getAuthorByName(@PathVariable String name) {
        try {
            Author author = authorService.findByName(name)
                    .orElseThrow(() -> new IllegalArgumentException("Author with name " + name + " not found"));
            return ResponseEntity.ok(author);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving author: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> addAuthor(@RequestBody Author author) {
        try {
            Author created = authorService.addAuthor(author);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding author: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAuthor(@PathVariable int id, @RequestBody Author author) {
        try {
            Author updated = authorService.updateById(id, author);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating author: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAuthor(@PathVariable int id) {
        try {
            authorService.deleteById(id);
            return ResponseEntity.ok("Author deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting author: " + e.getMessage());
        }
    }
}
