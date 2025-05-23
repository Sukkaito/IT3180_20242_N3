package vn.edu.hust.nmcnpm_20242_n3.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookCopyStatusEnum;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookLoanStatusEnum;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookRequestStatusEnum;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookRequestTypeEnum;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookCopy;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookLoan;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookRequest;
import vn.edu.hust.nmcnpm_20242_n3.entity.User;
import vn.edu.hust.nmcnpm_20242_n3.repository.BookCopyRepository;
import vn.edu.hust.nmcnpm_20242_n3.repository.BookRequestRepository;
import vn.edu.hust.nmcnpm_20242_n3.repository.UserRepository;
import vn.edu.hust.nmcnpm_20242_n3.service.BookLoanService;

@Service
public class BookRequestService {

    private final BookCopyRepository bookCopyRepository;
    private final BookLoanService bookLoanService;
    private final UserRepository userRepository;
    private final BookRequestRepository bookRequestRepository;

    @Autowired
    public BookRequestService(BookCopyRepository bookCopyRepository, BookLoanService bookLoanService,
            UserRepository userRepository, BookRequestRepository bookRequestRepository) {
        this.bookCopyRepository = bookCopyRepository;
        this.userRepository = userRepository;
        this.bookRequestRepository = bookRequestRepository;
        this.bookLoanService = bookLoanService;
    }

    public List<BookRequest> getAllRequests() {
        List<BookRequest> requests = (List<BookRequest>) bookRequestRepository.findAll();
        for (BookRequest request : requests) {
            if (request.getBookLoan() != null) {
                if (request.getBookCopy() == null) {
                    request.setBookCopy(request.getBookLoan().getBookCopy());
                }
                if (request.getUser() == null) {
                    request.setUser(request.getBookLoan().getUser());
                }
            }
        }
        return requests.stream()
                .sorted(Comparator.comparing((BookRequest br) -> br.getStatus() == BookRequestStatusEnum.PENDING ? 0 : 1)
                        .thenComparing(BookRequest::getCreatedAt))
                .collect(Collectors.toList());
    }

    @Transactional
    public BookRequest processRequest(String requestId, boolean approve) {
        BookRequest request = bookRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found with ID: " + requestId));

        if (request.getStatus() != BookRequestStatusEnum.PENDING) {
            throw new IllegalStateException("Request with ID " + requestId + " is not in PENDING state");
        }

        BookLoan bookLoan = request.getBookLoan();
        BookCopy bookCopy = request.getBookCopy();

        if (request.getType() == BookRequestTypeEnum.BORROWING) {
            if (bookLoan != null) {
                throw new IllegalStateException("BookLoan with ID " + bookLoan.getId() + " already exists for a new BORROWING request with ID " + requestId);
            }
            if (bookCopy == null) {
                throw new IllegalStateException("BookCopy not found for BORROWING request with ID: " + requestId);
            }
            if (!bookCopy.getStatus().equals(BookCopyStatusEnum.AVAILABLE)) {
                throw new IllegalStateException("BookCopy with ID " + bookCopy.getId() + " is already unavailable");
            }
            if (approve) {
                BookLoan newBookLoan = new BookLoan();
                newBookLoan.setBookCopy(bookCopy);
                newBookLoan.setUser(request.getUser());
                newBookLoan.setStatus(BookLoanStatusEnum.BORROWED);
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
            if (!BookLoanStatusEnum.BORROWED.equals(bookLoan.getStatus())) {
                throw new IllegalStateException("BookLoan with ID " + bookLoan.getId() + " is not in BORROWED state");
            }
            if (approve) {
                Date currentDate = new Date();
                bookLoan.setStatus(BookLoanStatusEnum.RETURNED);
                if (bookLoan.getActualReturnDate() == null) {
                    bookLoan.setActualReturnDate(currentDate);
                }
                bookLoan.setCurrentBookRequestId(null);
                bookLoanService.save(bookLoan);
                request.setStatus(BookRequestStatusEnum.ACCEPTED);
                if (bookCopy != null) {
                    bookCopy.setStatus(BookCopyStatusEnum.AVAILABLE);
                    bookCopyRepository.save(bookCopy);
                } else {
                    throw new IllegalStateException("BookCopy not found for RETURNING request with ID: " + requestId);
                }
            } else {
                request.setStatus(BookRequestStatusEnum.DENIED);
            }
        }

        return bookRequestRepository.save(request);
    }

    public List<BookRequest> listAllRequestsFromUser(String userId) {
        return (List<BookRequest>) bookRequestRepository.findByUserId(userId);
    }

    public BookRequest findRequestById(String id) {
        return bookRequestRepository.findById(id).get();
    }

    @Transactional
    public BookRequest newBorrowingRequest(String userId, Integer bookCopyId) {
        BookCopy bookCopy = bookCopyRepository.findById(bookCopyId)
                .orElseThrow(() -> new IllegalArgumentException("Book copy not found"));
        if (!bookCopy.getStatus().equals(BookCopyStatusEnum.AVAILABLE)) {
            throw new IllegalArgumentException("Book copy is not available");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<BookRequest> pendingRequests = bookRequestRepository.checkForOverlappingRequest(bookCopyId, userId,
                BookRequestTypeEnum.BORROWING);
        if (pendingRequests.size() > 0) {
            throw new IllegalArgumentException("You already have another pending borrowing request for this book!");
        }

        BookRequest bookRequest = new BookRequest();
        bookRequest.setUser(user);
        bookRequest.setBookCopy(bookCopy);
        bookRequest.setType(BookRequestTypeEnum.BORROWING);
        bookRequest.setStatus(BookRequestStatusEnum.PENDING);
        return bookRequestRepository.save(bookRequest);
    }

    @Transactional
    public BookRequest newReturningRequest(String userId, Integer bookCopyId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        BookLoan bookLoan = bookLoanService
                .findBookLoanByBookCopyIdAndUserIdAndStatus(bookCopyId, userId, BookLoanStatusEnum.BORROWED)
                .orElseThrow(() -> new IllegalArgumentException("Book copy is not borrowed by this user"));
        if (!bookLoan.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("User did not borrow this book copy!");
        }
        BookCopy bookCopy = bookCopyRepository.findById(bookCopyId)
                .orElseThrow(() -> new IllegalArgumentException("Book copy not found"));
        List<BookRequest> pendingRequests = bookRequestRepository.checkForOverlappingRequest(bookCopyId, userId,
                BookRequestTypeEnum.RETURNING);
        if (pendingRequests.size() > 0) {
            throw new IllegalArgumentException("You already have another pending returning request for this book!");
        }
        BookRequest bookRequest = new BookRequest();
        bookRequest.setUser(user);
        bookRequest.setBookCopy(bookCopy);
        bookRequest.setBookLoan(bookLoan); 
        bookRequest.setType(BookRequestTypeEnum.RETURNING);
        bookRequest.setStatus(BookRequestStatusEnum.PENDING);
        return bookRequestRepository.save(bookRequest);
    }

    @Transactional
    public BookRequest cancelRequest(String requestId) {
        BookRequest bookRequest = bookRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));
        if (!bookRequest.getStatus().equals(BookRequestStatusEnum.PENDING)) {
            throw new IllegalArgumentException("Request is not pending");
        }
        bookRequest.setStatus(BookRequestStatusEnum.CANCELED);
        return bookRequestRepository.save(bookRequest);
    }
}