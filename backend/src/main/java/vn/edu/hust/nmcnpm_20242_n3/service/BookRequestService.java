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
import vn.edu.hust.nmcnpm_20242_n3.dto.BookRequestDTO;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookCopy;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookLoan;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookRequest;
import vn.edu.hust.nmcnpm_20242_n3.entity.User;
import vn.edu.hust.nmcnpm_20242_n3.repository.BookCopyRepository;
import vn.edu.hust.nmcnpm_20242_n3.repository.BookRequestRepository;
import vn.edu.hust.nmcnpm_20242_n3.repository.UserRepository;

@Service
public class BookRequestService {

    private final BookCopyRepository bookCopyRepository;
    private final BookLoanService bookLoanService;
    private final UserRepository userRepository;
    private final BookRequestRepository bookRequestRepository;
    private final SubscriptionService subscriptionService;

    @Autowired
    public BookRequestService(BookCopyRepository bookCopyRepository, BookLoanService bookLoanService,
                              UserRepository userRepository, BookRequestRepository bookRequestRepository, SubscriptionService subscriptionService) {
        this.bookCopyRepository = bookCopyRepository;
        this.userRepository = userRepository;
        this.bookRequestRepository = bookRequestRepository;
        this.bookLoanService = bookLoanService;
        this.subscriptionService = subscriptionService;
    }

    public List<BookRequestDTO> getAllRequests() {
        List<BookRequest> requests = bookRequestRepository.findAll();
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
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public BookRequestDTO processRequest(String requestId, boolean approve) {
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
                subscriptionService.cancelSubscriptionAfterBorrowing(bookCopy.getId(), request.getUser().getId());
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

        return convertToDTO(bookRequestRepository.save(request));
    }

    public List<BookRequestDTO> listAllRequestsFromUser(String userId) {
        return bookRequestRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public BookRequestDTO findRequestById(String id) {
        return convertToDTO(bookRequestRepository.findById(id).get());
    }

    @Transactional
    public BookRequestDTO newBorrowingRequest(String userId, Integer bookCopyId) {
        BookCopy bookCopy = bookCopyRepository.findById(bookCopyId)
                .orElseThrow(() -> new IllegalArgumentException("Book copy not found"));
        if (!bookCopy.getStatus().equals(BookCopyStatusEnum.AVAILABLE)) {
            throw new IllegalArgumentException("Book copy is not available");
        }
        // Check if user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        // Check if user has pending requests
        List<BookRequest> pendingRequests = bookRequestRepository.checkForOverlappingRequest(bookCopyId, userId,
                BookRequestTypeEnum.BORROWING);
        if (pendingRequests.size() > 0) {
            throw new IllegalArgumentException("You already have another pending borrowing request for this book!");
        }

        // Create a new book request
        BookRequest bookRequest = new BookRequest();
        bookRequest.setUser(user);
        bookRequest.setBookCopy(bookCopy);
        bookRequest.setType(BookRequestTypeEnum.BORROWING);
        bookRequest.setStatus(BookRequestStatusEnum.PENDING);
        return convertToDTO(bookRequestRepository.save(bookRequest));
    }

    @Transactional
    public BookRequestDTO newReturningRequest(String userId, Integer bookCopyId) {
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
        // Create a new book request
        BookRequest bookRequest = new BookRequest();
        bookRequest.setUser(user);
        bookRequest.setBookCopy(bookCopy);
        bookRequest.setBookLoan(bookLoan);
        bookRequest.setType(BookRequestTypeEnum.RETURNING);
        bookRequest.setStatus(BookRequestStatusEnum.PENDING);
        return convertToDTO(bookRequestRepository.save(bookRequest));
    }

    @Transactional
    public void cancelRequest(String requestId) {
        BookRequest bookRequest = bookRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));
        if (!bookRequest.getStatus().equals(BookRequestStatusEnum.PENDING)) {
            throw new IllegalArgumentException("Request is not pending");
        }
        // Update book request status
        bookRequest.setStatus(BookRequestStatusEnum.CANCELED);
        convertToDTO(bookRequestRepository.save(bookRequest));
    }

    private BookRequestDTO convertToDTO(BookRequest bookRequest) {
        return new BookRequestDTO(
                bookRequest.getId(),
                bookRequest.getBookLoan() != null ? bookRequest.getBookLoan().getId() : null,
                bookRequest.getUser() != null ? bookRequest.getUser().getUserName() : null,
                bookRequest.getStatus(),
                bookRequest.getType(),
                bookRequest.getCreatedAt(),
                bookRequest.getUpdatedAt()
        );
    }
}