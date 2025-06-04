package vn.edu.hust.nmcnpm_20242_n3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import vn.edu.hust.nmcnpm_20242_n3.dto.SubscriptionDTO;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookCopy;
import vn.edu.hust.nmcnpm_20242_n3.service.SubscriptionService;

import java.util.List;

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
    public ResponseEntity<?> subscribeToBookCopy(@PathVariable Integer bookCopyId,@PathVariable String userId) {
        try {
            subscriptionService.subscribeToBookCopy(bookCopyId, userId);
            return ResponseEntity.ok("Subscription successful");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error subscribing: " + e.getMessage());
        }
    }

    // API để hủy đăng ký nhận thông báo
    @PutMapping("/unsubscribe/{subscriptionId}")
    public ResponseEntity<?> unsubscribeFromBookCopy(@PathVariable Integer subscriptionId ) {
        try {
            subscriptionService.unsubscribeFromBookCopy(subscriptionId);
            return ResponseEntity.ok("Unsubscription successful");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error unsubscribing: " + e.getMessage());
        }
    }

    // API để thông báo cho người dùng về bản sao sách
    @PostMapping("/notify")
    public ResponseEntity<?> notifyUsers(@RequestParam Integer bookCopyId) {
        try {
            subscriptionService.notifyUsersByBookCopy(bookCopyId);
            return ResponseEntity.ok("Notifications sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error notifying users: " + e.getMessage());
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



    @GetMapping("/subscriptions/{userId}")
    public ResponseEntity<List<SubscriptionDTO>> getUserSubscriptions(@PathVariable String userId) {
        try {
            List<SubscriptionDTO> subscriptions = subscriptionService.getUserBookCopies(userId)
                    .stream()
                    .map(bookCopy -> new SubscriptionDTO(bookCopy.getId(), bookCopy.getOriginalBook().getTitle()))
                    .toList();
            return ResponseEntity.ok(subscriptions);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
