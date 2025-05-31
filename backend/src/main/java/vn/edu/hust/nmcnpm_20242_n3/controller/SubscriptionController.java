package vn.edu.hust.nmcnpm_20242_n3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import vn.edu.hust.nmcnpm_20242_n3.service.SubscriptionService;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    // API để đăng ký nhận thông báo
    @PostMapping("/subscribe/{userId}/{bookCopyId}")
    public ResponseEntity<?> subscribeToBookCopy(@PathVariable int bookCopyId,@PathVariable String userId) {
        try {
            subscriptionService.subscribeToBookCopy(bookCopyId, userId);
            return ResponseEntity.ok("Subscription successful");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error subscribing: " + e.getMessage());
        }
    }

    // API để hủy đăng ký nhận thông báo
    @DeleteMapping("/unsubscribe/{userId}/{bookCopyId}")
    public ResponseEntity<?> unsubscribeFromBookCopy(@PathVariable int bookCopyId, @PathVariable String userId) {
        try {
            subscriptionService.unsubscribeFromBookCopy(bookCopyId, userId);
            return ResponseEntity.ok("Unsubscription successful");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error unsubscribing: " + e.getMessage());
        }
    }

    // API để thông báo cho người dùng về bản sao sách
    @PostMapping("/notify")
    public ResponseEntity<?> notifyUsers(@RequestParam int bookCopyId) {
        try {
            subscriptionService.notifyUsers(bookCopyId);
            return ResponseEntity.ok("Notifications sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error notifying users: " + e.getMessage());
        }
    }

    // API để mượn sách chỉ định và xử lý logic đăng ký nếu sách không khả dụng
    @PostMapping("/{userId}/borrow")
    public ResponseEntity<?> borrowBookCopy(@RequestParam int bookCopyId, @PathVariable String userId) {
        try {
            subscriptionService.borrowBookCopy(bookCopyId, userId);
            return ResponseEntity.ok("Borrowing process completed");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error borrowing book copy: " + e.getMessage());
        }
    }

    // API để mượn sách ngẫu nhiên và xử lý logic đăng ký nếu sách không khả dụng
    @PostMapping("/{userId}/borrow/rand")
    public ResponseEntity<?> borrowBookCopyRandom(@RequestParam int bookId, @PathVariable String userId) {
        try {
            subscriptionService.borrowBookCopyRandom(bookId, userId);
            return ResponseEntity.ok("Borrowing process completed");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error borrowing book copy: " + e.getMessage());
        }
    }

    // API để hủy đăng ký sau khi mượn sách
    @DeleteMapping("/cancel-after-borrow/{userId}/{bookCopyId}")
    public ResponseEntity<?> cancelSubscriptionAfterBorrowing(@PathVariable int bookCopyId, @PathVariable String userId) {
        try {
            subscriptionService.cancelSubscriptionAfterBorrowing(bookCopyId, userId);
            return ResponseEntity.ok("Subscription canceled successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error canceling subscription: " + e.getMessage());
        }
    }

    // API để quản trị viên thông báo thủ công
    @PostMapping("/notify-all")
    public ResponseEntity<?> notifyAllUsers() {
        try {
            subscriptionService.notifyAllUsers();
            return ResponseEntity.ok("All users notified successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error notifying users: " + e.getMessage());
        }
    }

    @Scheduled(cron = "0 0 4 * * ?")
    public void notifyUsersAutomatically() {
        subscriptionService.notifyAllUsers();
    }

    @GetMapping("/subscriptions/{userId}")
    public ResponseEntity<?> getUserSubscriptions(@PathVariable String userId) {
        try {
            var subscriptions = subscriptionService.getUserSubscriptions(userId);
            return ResponseEntity.ok(subscriptions);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving subscriptions: " + e.getMessage());
        }
    }

    @PostMapping("/notify/{bookCopyId}")
    public ResponseEntity<?> notifyUsersManually(@PathVariable int bookCopyId) {
        try {
            subscriptionService.notifyUsersByBookCopy(bookCopyId);
            return ResponseEntity.ok("Notifications sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error sending notifications: " + e.getMessage());
        }
    }
}
