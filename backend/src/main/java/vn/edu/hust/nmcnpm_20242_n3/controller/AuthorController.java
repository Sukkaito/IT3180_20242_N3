package vn.edu.hust.nmcnpm_20242_n3.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping // Get all
    public List<Author> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @GetMapping("/search/{name}") // Get by name
    public Author getAuthorByName(@PathVariable String name) {
        return authorService.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Author with name " + name + " not found"));
    }

    @PostMapping // Add new
    public Author addAuthor(@RequestBody Author author) {
        return authorService.addAuthor(author);
    }

    @PutMapping("/update/{id}") // Update by Id
    public Author updateAuthor(@PathVariable int id, @RequestBody Author author) {
        return authorService.updateById(id, author);
    }

    @DeleteMapping("/delete/{id}") // Delete by Id
    public void deleteAuthor(@PathVariable int id) {
        authorService.deleteById(id);
    }
}
