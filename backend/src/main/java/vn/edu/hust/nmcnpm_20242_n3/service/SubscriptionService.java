package vn.edu.hust.nmcnpm_20242_n3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookCopy;
import vn.edu.hust.nmcnpm_20242_n3.entity.Subscription;
import vn.edu.hust.nmcnpm_20242_n3.repository.BookCopyRepository;
import vn.edu.hust.nmcnpm_20242_n3.repository.SubscriptionRepository;
import vn.edu.hust.nmcnpm_20242_n3.repository.UserRepository;

import java.util.List;

@Service
public class SubscriptionService {

    private final BookCopyRepository bookCopyRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final JavaMailSender emailSender;

    @Autowired

    public SubscriptionService(BookCopyRepository bookCopyRepository, SubscriptionRepository subscriptionRepository,
                               UserRepository userRepository, JavaMailSender emailSender) {
        this.bookCopyRepository = bookCopyRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
        this.emailSender = emailSender;
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

    public void unsubscribeFromBookCopy(Integer subscriptionId) {
        // Find the subscription
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found"));

        // Mark the subscription as inactive
        subscription.setActive(false);
        subscriptionRepository.save(subscription);
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
            this.sendEmail(email,
                    "The book copy you subscribed to is now available. Book Title: "
                            + subscription.getBookCopy().getOriginalBook().getTitle()
                            + ", Copy ID: " + subscription.getBookCopy().getId());
        }
    }

    public void notifyUsersByBookCopy(int bookCopyId) {
        // Lấy tất cả các đăng ký cho bản sao sách cụ thể
        List<Subscription> subscriptions = subscriptionRepository.findAllByBookCopyIdAndActive(bookCopyId, true);

        if (subscriptions.isEmpty()) {
            System.out.println("No subscriptions found for book copy with ID: " + bookCopyId);
            return;
        }

        // Gửi thông báo cho từng người dùng
        for (Subscription subscription : subscriptions) {
            String email = subscription.getUser().getEmail();
            System.out.println("Notification sent to: " + email);
            this.sendEmail(email,
                    "The book copy you subscribed to is now available. Book Title: "
                            + subscription.getBookCopy().getOriginalBook().getTitle()
                            + ", Copy ID: " + subscription.getBookCopy().getId());
        }
    }

    private void sendEmail(String to, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Book Copy Available");
            message.setText(body);
            emailSender.send(message);
            System.out.println("Email sent to: " + to);
        } catch (Exception e) {
            System.err.println("Error sending email to " + to + ": " + e.getMessage());
        }
    }

    public List<BookCopy> getUserBookCopies(String userId) {
        // Lấy tất cả các đăng ký của người dùng
        List<Subscription> subscriptions = subscriptionRepository.findAllByUserId(userId);

        if (subscriptions.isEmpty()) {
            return List.of(); // Trả về danh sách rỗng nếu không có đăng ký nào
        }

        // Trả về danh sách các bản sao sách từ các đăng ký
        return subscriptions.stream()
                .map(Subscription::getBookCopy)
                .toList();
    }

    @Scheduled(cron = "0 0 4 * * ?")
    public void notifyUsersAutomatically() {
        this.notifyAllUsers();
    }
}

