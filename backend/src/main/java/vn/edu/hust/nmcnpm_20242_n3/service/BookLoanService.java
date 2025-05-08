package vn.edu.hust.nmcnpm_20242_n3.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.hust.nmcnpm_20242_n3.repository.BookLoanRepository;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookLoan;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookCopy;
import vn.edu.hust.nmcnpm_20242_n3.entity.User;

import java.util.List;


@Service
@RequiredArgsConstructor
public class BookLoanService {
    private BookLoanRepository bookLoanRepository;

    @Autowired
    public BookLoanService(BookLoanRepository bookLoanRepository) {
        this.bookLoanRepository = bookLoanRepository;
    }

    public boolean isUserInBookLoanList(String userId, String bookCopyId) {
        return bookLoanRepository.existsByUserIdAndBookCopyId(userId, bookCopyId);
    }

    public void removeUserFromBookLoanList(String userId) {
        bookLoanRepository.deleteByUserId(userId);
    }

    public boolean addUserToBookLoanList(BookCopy bookCopy,User user) {
        BookLoan bookLoan = new BookLoan(bookCopy ,user);
        return bookLoanRepository.save(bookLoan) != null;
    }

    public List<User> getUserListByBookCopyId(String bookCopyId) {
        return bookLoanRepository.getUserListByBookCopyId(bookCopyId);
    }
}
