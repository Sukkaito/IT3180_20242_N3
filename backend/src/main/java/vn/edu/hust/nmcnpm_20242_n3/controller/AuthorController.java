package vn.edu.hust.nmcnpm_20242_n3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.edu.hust.nmcnpm_20242_n3.dto.AuthorDTO;
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

    @GetMapping // Get All
    public List<AuthorDTO> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @GetMapping("/search/{name}") // Get By Name
    public AuthorDTO getAuthorByName(@PathVariable String name) {
        return authorService.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Author with name " + name + " not found"));
    }

    @PostMapping // Add New
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Only admin can add authors
    public AuthorDTO addAuthor(@RequestBody AuthorDTO dto) {
        return authorService.addAuthor(dto);
    }

    @PutMapping("/update/{id}") // Update By Id
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Only admin can update authors
    public AuthorDTO updateAuthor(@PathVariable int id, @RequestBody AuthorDTO dto) {
        return authorService.updateById(id, dto);
    }

    @DeleteMapping("/delete/{id}") // Delete By Id
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Only admin can delete authors
    public void deleteAuthor(@PathVariable int id) {
        authorService.deleteById(id);
    }
}