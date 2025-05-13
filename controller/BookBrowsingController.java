package vn.edu.hust.nmcnpm_20242_n3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hust.nmcnpm_20242_n3.dto.BookBrowsingDTO;
import vn.edu.hust.nmcnpm_20242_n3.service.BookBrowsingService;

import java.util.List;

@RestController
@RequestMapping("/api/browse")
public class BookBrowsingController {

    private final BookBrowsingService bookBrowsingService;

    @Autowired
    public BookBrowsingController(BookBrowsingService bookBrowsingService) {
        this.bookBrowsingService = bookBrowsingService;
    }

    @GetMapping
    public ResponseEntity<List<BookBrowsingDTO>> getAllBooksToBrowse() {
        return ResponseEntity.ok(bookBrowsingService.getAllBooksWithAvailableCopies());
    }
}
