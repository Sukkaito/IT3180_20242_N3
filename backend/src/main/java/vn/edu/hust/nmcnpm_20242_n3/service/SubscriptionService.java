package vn.edu.hust.nmcnpm_20242_n3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.stereotype.Service;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookCopyStatusEnum;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookCopy;
import vn.edu.hust.nmcnpm_20242_n3.entity.Subscription;
import vn.edu.hust.nmcnpm_20242_n3.repository.BookCopyRepository;
import vn.edu.hust.nmcnpm_20242_n3.repository.SubscriptionRepository;
import vn.edu.hust.nmcnpm_20242_n3.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {

    private final BookCopyRepository bookCopyRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final JavaMailSender emailSender;

    @Autowired
    private BookRequestService bookRequestService;

    public SubscriptionService(BookCopyRepository bookCopyRepository, SubscriptionRepository subscriptionRepository,
                               UserRepository userRepository, JavaMailSender mailSender ) {
        this.bookCopyRepository = bookCopyRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
        this.emailSender = mailSender; // Initialize with your email sender bean
    }

    public void subscribeToBookCopy(int bookCopyId, String userId) {
        // Check if the book copy exists
        BookCopy bookCopy = bookCopyRepository.findById(bookCopyId)
                .orElseThrow(() -> new IllegalArgumentException("Book copy not found"));

        // Create a new subscription
        Subscription subscription = new Subscription();
        subscription.setBookCopy(bookCopy);
        subscription.setUser(userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found")));
        subscription.setActive(true);

        // Save the subscription
        subscriptionRepository.save(subscription);
    }

    public void unsubscribeFromBookCopy(int bookCopyId, String userId) {
        // Find the subscription
        Subscription subscription = subscriptionRepository.findByBookCopyIdAndUserId(bookCopyId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found"));

        // Mark the subscription as inactive
        subscription.setActive(false);
        subscriptionRepository.save(subscription);
    }

    public void notifyUsers(int bookCopyId) {
        // Get all active subscriptions for the book copy
        List<Subscription> subscriptions = subscriptionRepository.findAllByBookCopyIdAndActive(bookCopyId, true);

        // Notify each user
        for (Subscription subscription : subscriptions) {
            String email = subscription.getUser().getEmail();
            this.sendEmail(email, "Book Copy Available", "The book copy you subscribed to is now available.");
            System.out.println("Notification sent to: " + email);
        }
    }

    public void borrowBookCopy(int bookCopyId, String userId) {
        BookCopy bookCopy = bookCopyRepository.findById(bookCopyId)
                .orElseThrow(() -> new IllegalArgumentException("Book copy not found"));

        if (bookCopy.getStatus() == BookCopyStatusEnum.UNAVAILABLE) {
            // Prompt the user to subscribe
            boolean wantsToSubscribe = promptUserForSubscription();
            if (wantsToSubscribe) {
                subscribeToBookCopy(bookCopyId, userId);
            }
        } else {
            try {
                bookRequestService.newBorrowingRequest(userId, bookCopyId);

            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }
    }

    public void borrowBookCopyRandom(int bookId, String userId) {
        // Lấy tất cả các bản sao sách có sẵn
        Optional<BookCopy> availableCopy = bookCopyRepository.findFirstByOriginalBook_BookIdAndStatus(bookId, BookCopyStatusEnum.AVAILABLE);

        if (availableCopy.isEmpty()) {
            boolean wantsToSubscribe = promptUserForSubscription();
            if (wantsToSubscribe) {
                subscribeToBookCopy(availableCopy.orElseThrow().getId(), userId);
            }
        }
        else {
            // Tạo yêu cầu mượn sách mới
            try {
                bookRequestService.newBorrowingRequest(userId, availableCopy.get().getId());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }
    }

    private boolean promptUserForSubscription() {
        return true; // This should be replaced with actual user interaction logic
    }

    public void cancelSubscriptionAfterBorrowing(int bookCopyId, String userId) {
        Subscription subscription = subscriptionRepository.findByBookCopyIdAndUserId(bookCopyId, userId)
                .orElse(null);

        if (subscription != null) {
            subscription.setActive(false);
            subscriptionRepository.save(subscription);
        }
    }

    public void notifyAllUsers() {
        // Lấy tất cả các đăng ký đang hoạt động
        List<Subscription> subscriptions = subscriptionRepository.findAllByActive(true);

        // Gửi thông báo cho từng người dùng
        for (Subscription subscription : subscriptions) {
            String email = subscription.getUser().getEmail();
            System.out.println("Notification sent to: " + email);
            this.sendEmail(email, "Book Copy Available", "The book copy you subscribed to is now available.");
        }
    }

    public Object getUserSubscriptions(String userId) {
        // Lấy tất cả các đăng ký của người dùng
        List<Subscription> subscriptions = subscriptionRepository.findAllByUserId(userId);

        if (subscriptions.isEmpty()) {
            return "No subscriptions found for user with ID: " + userId;
        }

        // Trả về danh sách các đăng ký
        return subscriptions;
    }

    public void notifyUsersByBookCopy(int bookCopyId) {
        // Lấy tất cả các đăng ký cho bản sao sách cụ thể
        List<Subscription> subscriptions = subscriptionRepository.findAllByBookCopyId(bookCopyId);

        if (subscriptions.isEmpty()) {
            System.out.println("No subscriptions found for book copy with ID: " + bookCopyId);
            return;
        }

        // Gửi thông báo cho từng người dùng
        for (Subscription subscription : subscriptions) {
            String email = subscription.getUser().getEmail();
            System.out.println("Notification sent to: " + email);
            this.sendEmail(email, "Book Copy Available", "The book copy you subscribed to is now available.");
        }
    }

    private void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            emailSender.send(message);
            System.out.println("Email sent to: " + to);
        } catch (Exception e) {
            System.err.println("Error sending email to " + to + ": " + e.getMessage());
        }
    }


}
