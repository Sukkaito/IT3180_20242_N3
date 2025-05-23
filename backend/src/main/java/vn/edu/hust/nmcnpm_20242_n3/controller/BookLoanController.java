package vn.edu.hust.nmcnpm_20242_n3.controller;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookLoanStatusEnum;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookCopy;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookLoan;
import vn.edu.hust.nmcnpm_20242_n3.entity.User;
import vn.edu.hust.nmcnpm_20242_n3.service.BookLoanService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/loaned")
@NoArgsConstructor
public class BookLoanController {

    private BookLoanService bookLoanService;

    @Autowired
    public BookLoanController(BookLoanService bookLoanService) {
        this.bookLoanService = bookLoanService;
    }

    @GetMapping("/{userId}")
    public List<BookLoan> getAllLoans(@PathVariable("userId") String userId) {
        return bookLoanService.getAllLoansByUserId(userId);
    }

    @PostMapping("/add/user/{bookCopyId}/{userId}")
    public boolean addUserToBookLoanList(@PathVariable int bookCopyId,@PathVariable String userId) {
        BookCopy bookCopy = bookLoanService.getBookCopyById(bookCopyId);
        User user = bookLoanService.getUserById(userId);

        if (bookCopy == null || user == null) {
            throw new IllegalArgumentException("BookCopy and User cannot be null");
        }

            BookLoan bookLoan = new BookLoan();
            bookLoan.setBookCopy(bookCopy);
            bookLoan.setUser(user);
            bookLoan.setLoan_duration(30); // Default loan duration
            bookLoan.setStatus(BookLoanStatusEnum.BORROWED);
            bookLoan.setLoanedAt(new Date());
        try {
            bookLoanService.save(bookLoan);
        } catch (Exception e){
            return false;
        }
        return true;


    }

    @GetMapping("/borrowed-book/{userId}")
    public ResponseEntity<?> getBorrowedBooksByUserId(@PathVariable String userId) {
        try {
            var borrowedBooks = bookLoanService.getBorrowedBooksByUserId(userId);
            return new ResponseEntity<>(borrowedBooks, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users/{bookCopyId}")
    public List<User> getUserListByBookCopyId(@PathVariable int bookCopyId) {
            return bookLoanService.getUserListByBookCopyId(bookCopyId)
                    .stream()
                    .map(user -> new User(user.getId(), user.getName(), user.getEmail()))
                    .toList();


        }




}
