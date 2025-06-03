package vn.edu.hust.nmcnpm_20242_n3.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookLoanStatusEnum;
import vn.edu.hust.nmcnpm_20242_n3.dto.BookCopyDTO;
import vn.edu.hust.nmcnpm_20242_n3.entity.Book;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookCopy;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookLoan;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookRequest;
import vn.edu.hust.nmcnpm_20242_n3.repository.BookCopyRepository;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookCopyStatusEnum;
import vn.edu.hust.nmcnpm_20242_n3.repository.BookLoanRepository;
import vn.edu.hust.nmcnpm_20242_n3.repository.BookRepository;
import vn.edu.hust.nmcnpm_20242_n3.repository.BookRequestRepository;

import java.util.List;
import java.util.Set;

import static org.springframework.data.repository.util.ClassUtils.ifPresent;


@RequiredArgsConstructor
@Service
public class BookCopyService {

    @Autowired
    private BookCopyRepository bookCopyRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookLoanRepository bookLoanRepository;
    @Autowired
    private BookRequestRepository bookRequestRepository;

    public List<BookCopyDTO> getAllAvailableBookCopies() {
        return bookCopyRepository.findByStatus(BookCopyStatusEnum.valueOf(BookCopyStatusEnum.AVAILABLE.name())).stream()
                .map(this::convertToDTO)
                .toList();
    }


    public List<BookCopyDTO> getBookCopiesByBookId(Integer bookId) {
        List<BookCopyDTO> bookCopies = bookCopyRepository.findByOriginalBook_BookId(bookId).stream()
                .map(this::convertToDTO)
                .toList();
        if (bookCopies.isEmpty()) {
            throw new RuntimeException("No book copies found for book ID: " + bookId);
        }
        return bookCopies;
    }

    private BookCopyDTO convertToDTO(BookCopy bookCopy) {
        return new BookCopyDTO(
                bookCopy.getId(),
                bookCopy.getOriginalBook().getBookId(),
                bookCopy.getStatus().name()
        );
    }

    public BookCopyDTO createBookCopy(BookCopyDTO bookCopyDTO) {
        Book book = bookRepository.findById(bookCopyDTO.getOriginalBookBookId())
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + bookCopyDTO.getOriginalBookBookId()));

        BookCopy bookCopy = new BookCopy();
        bookCopy.setOriginalBook(book);
        bookCopy.setStatus(BookCopyStatusEnum.AVAILABLE);

        BookCopy savedBookCopy = bookCopyRepository.save(bookCopy);

        return convertToDTO(savedBookCopy);
    }

    @Transactional
    public void deleteBookCopy(int id) {
        BookCopy bookCopy = bookCopyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book copy not found with ID: " + id));

        Set<BookLoan> bookLoans = bookCopy.getBookLoans();
        bookLoans.stream()
                .filter(loan -> loan.getStatus() != null && loan.getStatus() != BookLoanStatusEnum.RETURNED)
                .findAny()
                .ifPresent(loan -> {
                    throw new RuntimeException("Cannot delete book copy with active loans");
                });

        bookLoans.forEach(loan -> {
            loan.setBookCopy(null); // Remove the association with the book copy
            bookLoanRepository.save(loan); // Save the updated loan
        });

        Set<BookRequest> bookRequests = bookCopy.getBookRequests();
        bookRequests.forEach(request -> {
            request.setBookCopy(null); // Remove the association with the book copy
            bookRequestRepository.save(request); // Save the updated request
        });

        bookCopyRepository.delete(bookCopy);
    }
}
