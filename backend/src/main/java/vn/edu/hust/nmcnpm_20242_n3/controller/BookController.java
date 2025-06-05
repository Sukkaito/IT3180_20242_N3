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

    @PostMapping // Add New
    public ResponseEntity<?> addBook(@RequestBody BookDTO bookDTO) {
        try {
            Book book = bookService.addBook(bookDTO);
            return new ResponseEntity<>(bookService.convertToDTO(book), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping // Get All
    public ResponseEntity<?> getAllBooks() {
        List<BookDTO> books = bookService.findAllBooks().stream()
                .map(bookService::convertToDTO)
                .collect(Collectors.toList());

        if (books.isEmpty()) {
            return new ResponseEntity<>("No books found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/page/{page}/{size}") // Get Page by PageNumber(page) and size
    public ResponseEntity<?> getBooksByPage(@PathVariable int page, @PathVariable int size) {
        List<BookDTO> books = bookService.findBooksByPage(page, size).stream()
                .map(bookService::convertToDTO)
                .collect(Collectors.toList());

        if (books.isEmpty()) {
            return new ResponseEntity<>("No books found in this page", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(books, HttpStatus.OK);
    }


    @GetMapping("/search/id/{id}") // Get By Id
    public ResponseEntity<?> getBookById(@PathVariable int id) {
        try {
            Book book = bookService.searchById(id);
            return new ResponseEntity<>(bookService.convertToDTO(book), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search/title/{title}") // Get By Title
    public ResponseEntity<?> searchBooksByTitle(@PathVariable String title) {
        List<BookDTO> books = bookService.searchByTitle(title).stream()
                .map(bookService::convertToDTO)
                .collect(Collectors.toList());

        if (books.isEmpty()) {
            return new ResponseEntity<>("No books found with title containing: " + title, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/search/publisher/{publisherId}") // Get By PublisherId
    public ResponseEntity<?> searchBooksByPublisher(@PathVariable int publisherId) {
        List<BookDTO> books = bookService.searchByPublisherId(publisherId).stream()
                .map(bookService::convertToDTO)
                .collect(Collectors.toList());

        if (books.isEmpty()) {
            return new ResponseEntity<>("No books found with publisher name containing: " + publisherId, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/search/category/{categoryId}") // Get By CategoryId
    public ResponseEntity<?> searchBooksByCategory(@PathVariable int categoryId) {
        List<BookDTO> books = bookService.searchByCategoryId(categoryId).stream()
                .map(bookService::convertToDTO)
                .collect(Collectors.toList());

        if (books.isEmpty()) {
            return new ResponseEntity<>("No books found with category name containing: " + categoryId, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/search/author/{authorId}") // Get By AuthorId
    public ResponseEntity<?> searchBooksByAuthor(@PathVariable int authorId) {
        List<BookDTO> books = bookService.searchByAuthorId(authorId).stream()
                .map(bookService::convertToDTO)
                .collect(Collectors.toList());

        if (books.isEmpty()) {
            return new ResponseEntity<>("No books found with author name containing: " + authorId, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @PutMapping("/update/{id}") // Update By Id
    public ResponseEntity<?> updateBookByTitle(@PathVariable int id, @RequestBody BookDTO bookDTO){
        try{
            Book updatedBook= bookService.updateByTitle(id, bookDTO);
            return new ResponseEntity<>(updatedBook, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}") // Delete By Id
    public ResponseEntity<?> deleteBookById(@PathVariable int id) {
        try {
            bookService.deleteById(id);
            return new ResponseEntity<>("Book deleted successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}