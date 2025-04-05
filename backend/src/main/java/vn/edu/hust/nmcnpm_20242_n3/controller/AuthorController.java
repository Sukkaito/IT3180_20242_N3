package vn.edu.hust.nmcnpm_20242_n3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hust.nmcnpm_20242_n3.dto.AuthorDTO;
import vn.edu.hust.nmcnpm_20242_n3.entity.Author;
import vn.edu.hust.nmcnpm_20242_n3.entity.Publisher;
import vn.edu.hust.nmcnpm_20242_n3.service.AuthorService;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    public ResponseEntity<Author> addAuthor(@RequestBody AuthorDTO authorDTO) {
        try {
            Author author = authorService.addAuthor(authorDTO);
            return new ResponseEntity<>(author, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Author> findAuthorByName(@RequestParam String name) {
        return authorService.findByName(name)
                .map(author -> new ResponseEntity<>(author, HttpStatus.OK))
                .orElseThrow(() -> new IllegalArgumentException("Author not found"));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateAuthorByName(@RequestParam String name, @RequestBody AuthorDTO authorDTO) {
        try {
            Author updatedAuthor = authorService.updateByName(name, authorDTO);
            return new ResponseEntity<>(updatedAuthor, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAuthorByName(@RequestParam String name) {
        try {
            authorService.deleteByName(name);
            return new ResponseEntity<>("Author deleted successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}