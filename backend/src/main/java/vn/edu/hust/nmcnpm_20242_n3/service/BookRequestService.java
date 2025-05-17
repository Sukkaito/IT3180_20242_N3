package vn.edu.hust.nmcnpm_20242_n3.service;

import java.util.List;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import vn.edu.hust.nmcnpm_20242_n3.entity.BookRequest;
import vn.edu.hust.nmcnpm_20242_n3.entity.User;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookCopy;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookLoan;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookCopyStatusEnum;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookLoanStatusEnum;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookRequestStatusEnum;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookRequestTypeEnum;
import vn.edu.hust.nmcnpm_20242_n3.service.BookLoanService;

import vn.edu.hust.nmcnpm_20242_n3.repository.BookCopyRepository;
import vn.edu.hust.nmcnpm_20242_n3.repository.BookRequestRepository;
import vn.edu.hust.nmcnpm_20242_n3.repository.UserRepository;

@Service
public class BookRequestService {
    private BookCopyRepository bookCopyRepository;
    private BookLoanService bookLoanService;
    private UserRepository userRepository;
    private BookRequestRepository bookRequestRepository;

    public BookRequestService(BookCopyRepository bookCopyRepository, BookLoanService bookLoanService,
            UserRepository userRepository, BookRequestRepository bookRequestRepository) {
        this.bookCopyRepository = bookCopyRepository;
        this.userRepository = userRepository;
        this.bookRequestRepository = bookRequestRepository;
        this.bookLoanService = bookLoanService;
    }

    public List<BookRequest> listAllRequestsFromUser(String userId) {
        return (List<BookRequest>) bookRequestRepository.findByUserId(userId);
    }

    public BookRequest findRequestById(Integer id) {
        return bookRequestRepository.findById(id).get();
    }

    @Transactional
    public BookRequest newBorrowingRequest(String userId, Integer bookCopyId) {

        BookCopy bookCopy = bookCopyRepository.findById(bookCopyId)
                .orElseThrow(() -> new IllegalArgumentException("Book copy not found"));
        if ((!bookCopy.getStatus().equals(BookCopyStatusEnum.AVAILABLE))) {
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
        return bookRequestRepository.save(bookRequest);
    }

    @Transactional
    public BookRequest newReturningRequest(String userId, Integer bookCopyId) {
        // Check if user exists
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
        bookRequest.setType(BookRequestTypeEnum.RETURNING);
        bookRequest.setStatus(BookRequestStatusEnum.PENDING);
        return bookRequestRepository.save(bookRequest);
    }

    @Transactional
    public BookRequest cancelRequest(Integer requestId) {
        BookRequest bookRequest = bookRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));
        if (!bookRequest.getStatus().equals(BookRequestStatusEnum.PENDING)) {
            throw new IllegalArgumentException("Request is not pending");
        }
        // Update book request status
        bookRequest.setStatus(BookRequestStatusEnum.CANCELED);
        return bookRequestRepository.save(bookRequest);
    }
}
