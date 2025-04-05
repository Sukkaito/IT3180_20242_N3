package vn.edu.hust.nmcnpm_20242_n3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hust.nmcnpm_20242_n3.dto.BookDTO;
import vn.edu.hust.nmcnpm_20242_n3.entity.Book;
import vn.edu.hust.nmcnpm_20242_n3.service.BookService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<?> addBook(@RequestBody BookDTO bookDTO) {
        try {
            Book book = bookService.addBook(bookDTO);
            return new ResponseEntity<>(bookService.convertToDTO(book), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/search/title")
    public ResponseEntity<?> searchBooksByTitle(@RequestParam String title) {
        List<BookDTO> books = bookService.searchByTitle(title).stream()
                .map(bookService::convertToDTO)
                .collect(Collectors.toList());

        if (books.isEmpty()) {
            return new ResponseEntity<>("No books found with title containing: " + title, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/search/publisher")
    public ResponseEntity<?> searchBooksByPublisher(@RequestParam String publisherName) {
        List<BookDTO> books = bookService.searchByPublisherName(publisherName).stream()
                .map(bookService::convertToDTO)
                .collect(Collectors.toList());

        if (books.isEmpty()) {
            return new ResponseEntity<>("No books found with publisher name containing: " + publisherName, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/search/category")
    public ResponseEntity<?> searchBooksByCategory(@RequestParam String categoryName) {
        List<BookDTO> books = bookService.searchByCategoryName(categoryName).stream()
                .map(bookService::convertToDTO)
                .collect(Collectors.toList());

        if (books.isEmpty()) {
            return new ResponseEntity<>("No books found with category name containing: " + categoryName, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/search/author")
    public ResponseEntity<?> searchBooksByAuthor(@RequestParam String authorName) {
        List<BookDTO> books = bookService.searchByAuthorName(authorName).stream()
                .map(bookService::convertToDTO)
                .collect(Collectors.toList());

        if (books.isEmpty()) {
            return new ResponseEntity<>("No books found with author name containing: " + authorName, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateBookByTitle(@RequestParam String title, @RequestBody BookDTO bookDTO){
        try{
            Book updatedBook= bookService.updateByTitle(title, bookDTO);
            return new ResponseEntity<>(updatedBook, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteBookByTitle(@RequestParam String title) {
        try {
            bookService.deleteByTitle(title);
            return new ResponseEntity<>("Book deleted successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}