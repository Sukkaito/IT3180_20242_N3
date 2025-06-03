package vn.edu.hust.nmcnpm_20242_n3.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookCopyStatusEnum;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookLoanStatusEnum;
import vn.edu.hust.nmcnpm_20242_n3.dto.BookLoanDTO;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookCopy;
import vn.edu.hust.nmcnpm_20242_n3.entity.User;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookLoan;
import vn.edu.hust.nmcnpm_20242_n3.repository.BookCopyRepository;
import vn.edu.hust.nmcnpm_20242_n3.repository.BookLoanRepository;
import vn.edu.hust.nmcnpm_20242_n3.repository.UserRepository;

@EnableScheduling
@Service
public class BookLoanService {

    private final BookCopyRepository bookCopyRepository;
    private final BookLoanRepository bookLoanRepository;
    private final UserRepository userRepository;


    @Autowired
    public BookLoanService(BookCopyRepository bookCopyRepository, BookLoanRepository bookLoanRepository,
            UserRepository userRepository) {
        this.bookCopyRepository = bookCopyRepository;
        this.bookLoanRepository = bookLoanRepository;

        this.userRepository = userRepository;

    }

    public List<BookLoanDTO> getAllLoansByUserId(String userId) {
        List<BookLoan> bookLoans = bookLoanRepository.findByUserId(userId);
        return bookLoans.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Transactional
    public BookLoan newBookLoan(String userId, Integer bookCopyId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        BookCopy bookCopy = bookCopyRepository.findById(bookCopyId)
                .orElseThrow(() -> new IllegalArgumentException("Book copy not found"));
        if (!bookCopy.getStatus().equals(BookCopyStatusEnum.AVAILABLE)) {
            throw new IllegalArgumentException("Book copy is not available");
        }

        BookLoan bookLoan = new BookLoan();
        bookLoan.setUser(user);
        bookLoan.setBookCopy(bookCopy);
        bookLoan.setLoan_duration(30);
        bookLoan.setStatus(BookLoanStatusEnum.BORROWED);
        bookLoan.setLoanedAt(new Date());

        bookCopyRepository.save(bookCopy);
        return bookLoanRepository.save(bookLoan);
    }

    @Scheduled(cron = "0 0 4 * * ?") // Every day at 4 AM
    @Transactional
    public void checkOverdueBookLoans() {
        List<BookLoan> borrowedLoans = bookLoanRepository.findByStatus(BookLoanStatusEnum.BORROWED);
        Date currentDate = new Date();

        for (BookLoan loan : borrowedLoans) {
            if (loan.getDueDate() != null && loan.getDueDate().before(currentDate)) {
                loan.setStatus(BookLoanStatusEnum.OVERDUE);
                User user = loan.getUser();
                bookLoanRepository.save(loan);
            }
        }
    }

    public Optional<BookLoan> findBookLoanByBookCopyIdAndUserIdAndStatus(int bookCopyId, String userId,
            BookLoanStatusEnum status) {
        return bookLoanRepository.findByBookCopyIdAndUserIdAndStatus(userId, bookCopyId, status);
    }


    public void save(BookLoan bookLoan) {
        bookLoanRepository.save(bookLoan);
    }

    public List<BookLoanDTO> getBorrowHistoryByBookCopyId(int bookCopyId) {
        return bookLoanRepository.findAllByBookCopyId(bookCopyId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<BookLoanDTO> getOverdueLoans() {
        return bookLoanRepository.findByStatus(BookLoanStatusEnum.OVERDUE).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<BookLoanDTO> getAllBookLoans() {
        List<BookLoan> bookLoans = (List<BookLoan>) bookLoanRepository.findAll();
        return bookLoans.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public BookLoanDTO convertToDTO(BookLoan bookLoan) {
        return new BookLoanDTO(
            bookLoan.getId(),
            bookLoan.getBookCopy() != null ? bookLoan.getBookCopy().getId() : -1,
            bookLoan.getBookCopy() != null ? bookLoan.getBookCopy().getOriginalBook().getTitle() : null,
            bookLoan.getUser().getUserName(),
            bookLoan.getLoanedAt(),
            bookLoan.getDueDate(),
            bookLoan.getActualReturnDate(),
            bookLoan.getStatus().toString(),
            bookLoan.getLoanedAt(),
            bookLoan.getUpdatedAt()
        );
    }
}