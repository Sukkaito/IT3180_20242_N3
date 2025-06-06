package vn.edu.hust.nmcnpm_20242_n3.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookRequest;
import vn.edu.hust.nmcnpm_20242_n3.entity.Fine;
import vn.edu.hust.nmcnpm_20242_n3.entity.Subscription;
import vn.edu.hust.nmcnpm_20242_n3.entity.User;
import vn.edu.hust.nmcnpm_20242_n3.repository.BookRequestRepository;
import vn.edu.hust.nmcnpm_20242_n3.repository.FineRepository;
import vn.edu.hust.nmcnpm_20242_n3.repository.SubscriptionRepository;
import vn.edu.hust.nmcnpm_20242_n3.repository.UserRepository;

import java.util.Optional;

/**
 * Service for handling authentication-related operations.
 * This service provides methods to check if the current user is authorized
 * to access certain resources based on their user ID.
 */
@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final BookRequestRepository bookRequestRepository;
    private final FineRepository fineRepository;
    private final SubscriptionRepository subscriptionRepository;

    public AuthenticationService(UserRepository userRepository, BookRequestRepository bookRequestRepository, FineRepository fineRepository, SubscriptionRepository subscriptionRepository) {
        this.userRepository = userRepository;
        this.bookRequestRepository = bookRequestRepository;
        this.fineRepository = fineRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    /**
     * Checks if the currently authenticated user is authorized to access the resource
     * associated with the given user ID.
     *
     * @param userId The ID of the user to check authorization against.
     * @return true if the current user is authorized, false otherwise.
     */
    public boolean isAuthorizedUser(String userId) {
        // Get current authenticated user
        Authentication authentication = getAuthentication();
        if (authentication == null) return false;

        String username = authentication.getName();
        Optional<User> userOpt = userRepository.findByUserName(username);
        if (userOpt.isEmpty()) return false;
        User user = userOpt.get();

        // Check if the user is the one being accessed
        return user.getId().equals(userId);
    }

    /**
     * Checks if the currently authenticated user is authorized to access the book request
     * associated with the given request ID.
     *
     * @param requestId The ID of the book request to check authorization against.
     * @return true if the current user is authorized, false otherwise.
     */
    public boolean isAuthorizedRequest(String requestId) {
        Optional<BookRequest> requestOpt = bookRequestRepository.findById(requestId);
        if (requestOpt.isEmpty()) return false;
        BookRequest request = requestOpt.get();

        // Check if the user is the one being accessed
        return request.getUser().getUsername().equals(getCurrentUsername());
    }

    /**
     * Checks if the currently authenticated user is authorized to access the fine
     * associated with the given fine ID.
     *
     * @param fineId The ID of the fine to check authorization against.
     * @return true if the current user is authorized, false otherwise.
     */
    public boolean isAuthorizedFine(String fineId) {
        Optional<Fine> fineOpt = fineRepository.findById(fineId);
        if (fineOpt.isEmpty()) return false;
        Fine fine = fineOpt.get();

        // Check if the user is the one being accessed
        return fine.getUser().getUsername().equals(getCurrentUsername());
    }

    /**
     * Checks if the currently authenticated user is authorized to access the subscription
     * associated with the given subscription ID.
     *
     * @param subscriptionId The ID of the subscription to check authorization against.
     * @return true if the current user is authorized, false otherwise.
     */
    public boolean isAuthorizedSubscription(Integer subscriptionId) {
        Optional<Subscription> subscriptionOpt = subscriptionRepository.findById(subscriptionId);
        if (subscriptionOpt.isEmpty()) return false;
        Subscription subscription = subscriptionOpt.get();

        // Check if the user is the one being accessed
        return subscription.getUser().getUsername().equals(getCurrentUsername());
    }

    /**
     * Retrieves the current authenticated user from the security context.
     *
     * @return The Authentication object of the current user, or null if not authenticated.
     */
    private static Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        return authentication;
    }

    /**
     * Retrieves the username of the currently authenticated user.
     * As a result, it only gets invoked if authentication is guaranteed to be non-null.
     * @return The username of the current user.
     * @throws IllegalStateException if the authentication is null.
     */
    public String getCurrentUsername() {
        Authentication authentication = getAuthentication();
        assert authentication != null;
        return authentication.getName();
    }
}
