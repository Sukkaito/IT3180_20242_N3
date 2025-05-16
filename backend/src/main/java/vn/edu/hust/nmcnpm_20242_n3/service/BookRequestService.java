package vn.edu.hust.nmcnpm_20242_n3.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookLoan;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookRequest;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookCopy;
import vn.edu.hust.nmcnpm_20242_n3.repository.BookRequestRepository;
import vn.edu.hust.nmcnpm_20242_n3.repository.BookCopyRepository;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookLoanStatusEnum;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookRequestStatusEnum;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookRequestTypeEnum;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookCopyStatusEnum;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;

@Service
public class BookRequestService {

    private final BookRequestRepository bookRequestRepository;
    private final BookLoanService bookLoanService;
    private final BookCopyRepository bookCopyRepository;

    @Autowired
    public BookRequestService(BookRequestRepository bookRequestRepository, BookLoanService bookLoanService, BookCopyRepository bookCopyRepository) {
        this.bookRequestRepository = bookRequestRepository;
        this.bookLoanService = bookLoanService;
        this.bookCopyRepository = bookCopyRepository;
    }

    public List<BookRequest> getAllRequests() {
        List<BookRequest> requests = (List<BookRequest>) bookRequestRepository.findAll();
        return requests.stream()
            .sorted(Comparator.comparing((BookRequest br) -> br.getStatus() == BookRequestStatusEnum.PENDING ? 0 : 1)
                .thenComparing(BookRequest::getCreatedAt))
            .collect(Collectors.toList());
    }

    @Transactional
    public BookRequest processRequest(String requestId, boolean approve, String bookCopyId) {
        BookRequest request = bookRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found with ID: " + requestId));

        if (request.getStatus() != BookRequestStatusEnum.PENDING) {
            throw new IllegalStateException("Request with ID " + requestId + " is not in PENDING state");
        }

        BookLoan bookLoan = request.getBookLoan();

        if (request.getType() == BookRequestTypeEnum.BORROWING) {
            if (bookLoan != null) {
                throw new IllegalStateException("BookLoan with ID " + bookLoan.getId() + " already exists for a new BORROWING request with ID " + requestId);
            }
            if (approve) {
                if (bookCopyId == null) {
                    throw new IllegalArgumentException("bookCopyId is required for approving a BORROWING request");
                }
                BookCopy bookCopy = bookCopyRepository.findById(bookCopyId)
                        .orElseThrow(() -> new IllegalArgumentException("BookCopy not found with ID: " + bookCopyId));
                if (bookCopy.getStatus() == BookCopyStatusEnum.UNAVAILABLE) {
                    throw new IllegalStateException("BookCopy with ID " + bookCopyId + " is already unavailable, conflict detected");
                }
                BookLoan newBookLoan = new BookLoan();
                newBookLoan.setBookCopy(bookCopy);
                newBookLoan.setStatus(BookLoanStatusEnum.BORROWED);
                newBookLoan.setLoanDate(LocalDateTime.now());
                newBookLoan.setCurrentBookRequestId(null);
                bookLoanService.save(newBookLoan);
                request.setBookLoan(newBookLoan);
                request.setStatus(BookRequestStatusEnum.ACCEPTED);
                bookCopy.setStatus(BookCopyStatusEnum.UNAVAILABLE);
                bookCopyRepository.save(bookCopy);
            } else {
                request.setStatus(BookRequestStatusEnum.DENIED);
            }
        } else if (request.getType() == BookRequestTypeEnum.RETURNING) {
            if (bookLoan == null) {
                throw new IllegalStateException("Associated BookLoan not found for RETURNING request with ID: " + requestId);
            }
            if (approve) {
                bookLoan.setStatus(BookLoanStatusEnum.RETURNED);
                bookLoan.setActualReturnDate(LocalDateTime.now());
                bookLoan.setCurrentBookRequestId(null);
                bookLoanService.save(bookLoan);
                request.setStatus(BookRequestStatusEnum.ACCEPTED);
                BookCopy bookCopy = bookLoan.getBookCopy();
                if (bookCopy != null) {
                    bookCopy.setStatus(BookCopyStatusEnum.AVAILABLE);
                    bookCopyRepository.save(bookCopy);
                } else {
                    throw new IllegalStateException("BookCopy not found for existing BookLoan with ID: " + bookLoan.getId());
                }
            } else {
                // Từ chối yêu cầu trả, không thay đổi trạng thái BookLoan
                request.setStatus(BookRequestStatusEnum.DENIED);
            }
        }

        return bookRequestRepository.save(request);
    }
}