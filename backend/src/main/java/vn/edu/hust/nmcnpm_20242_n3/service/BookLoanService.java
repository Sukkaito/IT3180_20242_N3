package vn.edu.hust.nmcnpm_20242_n3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookLoan;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookRequest;
import vn.edu.hust.nmcnpm_20242_n3.repository.BookLoanRepository;
import vn.edu.hust.nmcnpm_20242_n3.repository.BookRequestRepository;

@Service
public class BookLoanService {

    private final BookLoanRepository bookLoanRepository;
    private final BookRequestRepository bookRequestRepository;

    @Autowired
    public BookLoanService(BookLoanRepository bookLoanRepository, BookRequestRepository bookRequestRepository) {
        this.bookLoanRepository = bookLoanRepository;
        this.bookRequestRepository = bookRequestRepository;
    }

    public BookLoan findByRequestId(String requestId) {
        BookRequest request = bookRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Book request not found with ID: " + requestId));
        return request.getBookLoan();
    }

    public BookLoan save(BookLoan bookLoan) {
        return bookLoanRepository.save(bookLoan);
    }
}