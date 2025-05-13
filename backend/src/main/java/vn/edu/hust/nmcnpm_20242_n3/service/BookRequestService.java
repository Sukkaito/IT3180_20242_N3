package vn.edu.hust.nmcnpm_20242_n3.service;

import java.util.List;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import vn.edu.hust.nmcnpm_20242_n3.entity.BookRequest;
import vn.edu.hust.nmcnpm_20242_n3.entity.User;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookCopy;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookCopyStatusEnum;
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
    public BookRequestService(BookCopyRepository bookCopyRepository, BookLoanService bookLoanService, UserRepository userRepository, BookRequestRepository bookRequestRepository) {
        this.bookCopyRepository = bookCopyRepository;
        this.userRepository = userRepository;
        this.bookRequestRepository = bookRequestRepository;
        this.bookLoanService = bookLoanService;
    }
    public List<BookRequest> listAllRequestsFromUser(String userId) {
        return (List<BookRequest>) bookRequestRepository.listAllRequestsFromUser(userId);
    }
    public BookRequest findRequestById(String id){
        return bookRequestRepository.findById(id).get();
    }
    @Transactional
    public BookRequest newBorrowingRequest(String userId, String bookCopyId) {
        // Check if user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        // Check if user can borrow books
        if (user.getStatus() != vn.edu.hust.nmcnpm_20242_n3.constant.UserStatusEnum.FREE) {
            throw new IllegalArgumentException("User is not allowed to borrow books");
        }
        //Check if user has pending requests
        List<BookRequest> pendingRequests = bookRequestRepository.checkIfUserHasRequest(bookCopyId, userId);
        if(pendingRequests.size() > 0) {
            throw new IllegalArgumentException("You already have pending request for this book!");
        }
 
        BookCopy bookCopy = bookCopyRepository.findById(bookCopyId).get();
        if(bookCopy== null) {
            throw new IllegalArgumentException("Book copy not found");
        }
        if(bookCopy.getStatus() != BookCopyStatusEnum.AVAILABLE) {
            throw new IllegalArgumentException("Book copy is not available");
        }

        // Create a new book request
        BookRequest bookRequest = new BookRequest();
        bookRequest.setUser(user);
        bookRequest.setBookCopy(bookCopy);
        bookRequest.setStatus(BookRequestStatusEnum.PENDING);
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
        bookRequest.setType(BookRequestTypeEnum.RETURNING);
        bookRequest.setBookCopy(bookCopy);
        bookRequest.setStatus(BookRequestStatusEnum.PENDING);
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
