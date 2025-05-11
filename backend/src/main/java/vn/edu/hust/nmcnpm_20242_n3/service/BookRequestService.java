package vn.edu.hust.nmcnpm_20242_n3.service;

import java.util.List;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import vn.edu.hust.nmcnpm_20242_n3.entity.BookRequest;
import vn.edu.hust.nmcnpm_20242_n3.entity.User;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookCopy;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookCopyStatusEnum;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookRequestStatusEnum;


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
    public BookRequestService(BookCopyRepository bookCopyRepository, BookLoanService bookLoanService, UserRepository userRepository, BookRequestRepository bookRequestRepository) {
        this.bookCopyRepository = bookCopyRepository;
        this.userRepository = userRepository;
        this.bookRequestRepository = bookRequestRepository;
        this.bookLoanService = bookLoanService;
    }
    public List<BookRequest> getAllRequests() {
        return (List<BookRequest>) bookRequestRepository.findAll();
    }
    @Transactional
    public BookRequest newBorrowingRequest(String userId, Integer bookId) {
        // Check if user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        // Check if user can borrow books
        if (user.getStatus() != vn.edu.hust.nmcnpm_20242_n3.constant.UserStatusEnum.FREE) {
            throw new IllegalArgumentException("User is not allowed to borrow books");
        }
        //Check if user has pending requests
        List<BookRequest> pendingRequests = bookRequestRepository.checkIfUserHasRequest(bookId, userId);
        if(pendingRequests.size() > 0) {
            throw new IllegalArgumentException("You already have pending request for this book!");
        }
 
        BookCopy bookCopy = bookCopyRepository.findFirstByOriginalBook_BookIdAndStatus(bookId, vn.edu.hust.nmcnpm_20242_n3.constant.BookCopyStatusEnum.AVAILABLE)
                .orElseThrow(() -> new IllegalArgumentException("No available copies for this book"));

        // Create a new book request
        BookRequest bookRequest = new BookRequest();
        bookRequest.setUser(user);
        bookRequest.setBookCopy(bookCopy);
        bookCopy.setStatus(BookCopyStatusEnum.UNAVAILABLE);
        bookRequest.setStatus(BookRequestStatusEnum.PENDING);
        return bookRequestRepository.save(bookRequest);
    }
    @Transactional
    public BookRequest acceptBorrowingRequest(String requestId) {
        BookRequest bookRequest = bookRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));
        if (bookRequest.getStatus() != BookRequestStatusEnum.PENDING) {
            throw new IllegalArgumentException("Request is not pending");
        }
        // Update book request status
        bookRequest.setStatus(BookRequestStatusEnum.ACCEPTED);  
        bookLoanService.newBookLoan(bookRequest.getUser().getId(), bookRequest.getBookCopy().getId());
        return bookRequestRepository.save(bookRequest);
    }
    @Transactional
    public BookRequest rejectBorrowingRequest(String requestId) {
        BookRequest bookRequest = bookRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));
        if (bookRequest.getStatus() != BookRequestStatusEnum.PENDING) {
            throw new IllegalArgumentException("Request is not pending");
        }
        // Update book request status
        bookRequest.setStatus(BookRequestStatusEnum.REJECTED);
        BookCopy bookCopy = bookRequest.getBookCopy();
        bookCopy.setStatus(BookCopyStatusEnum.AVAILABLE);
        bookCopyRepository.save(bookCopy);
        return bookRequestRepository.save(bookRequest);
    }
    @Transactional
    public BookRequest cancelBorrowingRequest(String requestId) {
        BookRequest bookRequest = bookRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));  
        if (bookRequest.getStatus() != BookRequestStatusEnum.PENDING) {
            throw new IllegalArgumentException("Request is not pending");
        }
        // Update book request status
        bookRequest.setStatus(BookRequestStatusEnum.CANCELED);
        BookCopy bookCopy = bookRequest.getBookCopy();
        bookCopy.setStatus(BookCopyStatusEnum.AVAILABLE);   
        bookCopyRepository.save(bookCopy);
        return bookRequestRepository.save(bookRequest);    
    }
    @Transactional
    public BookRequest newReturningRequest(String userId, String bookCopyId) {
        // Check if user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        BookCopy bookCopy = bookCopyRepository.findById(bookCopyId)
                .orElseThrow(() -> new IllegalArgumentException("Book copy not found"));
        if (bookCopy.getStatus() != BookCopyStatusEnum.UNAVAILABLE) {
            throw new IllegalArgumentException("Book copy is not borrowed");
        }
        // Create a new book request
        BookRequest bookRequest = new BookRequest();
        bookRequest.setUser(user);
        bookRequest.setBookCopy(bookCopy);
        bookRequest.setStatus(BookRequestStatusEnum.PENDING);
        return bookRequestRepository.save(bookRequest);
    }
    @Transactional
    public BookRequest acceptReturningRequest(String requestId) {
        BookRequest bookRequest = bookRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));
        if (bookRequest.getStatus() != BookRequestStatusEnum.PENDING) {
            throw new IllegalArgumentException("Request is not pending");
        }
        // Update book request status
        bookRequest.setStatus(BookRequestStatusEnum.ACCEPTED);
        bookLoanService.ReturningBook(bookRequest.getBookCopy().getId());
        return bookRequestRepository.save(bookRequest);
    }
    @Transactional
    public BookRequest rejectReturningRequest(String requestId) {
        BookRequest bookRequest = bookRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));
        if (bookRequest.getStatus() != BookRequestStatusEnum.PENDING) {
            throw new IllegalArgumentException("Request is not pending");
        }   
        // Update book request status
        bookRequest.setStatus(BookRequestStatusEnum.REJECTED);
        BookCopy bookCopy = bookRequest.getBookCopy();
        bookCopyRepository.save(bookCopy);
        return bookRequestRepository.save(bookRequest);
    }
    @Transactional
    public BookRequest cancelReturningRequest(String requestId) {
        BookRequest bookRequest = bookRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));
        if (bookRequest.getStatus() != BookRequestStatusEnum.PENDING) {
            throw new IllegalArgumentException("Request is not pending");
        }
        // Update book request status
        bookRequest.setStatus(BookRequestStatusEnum.CANCELED);
        BookCopy bookCopy = bookRequest.getBookCopy();
        bookCopyRepository.save(bookCopy);
        return bookRequestRepository.save(bookRequest);
    }
}
