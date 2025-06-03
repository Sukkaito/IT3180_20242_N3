package vn.edu.hust.nmcnpm_20242_n3.controller;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hust.nmcnpm_20242_n3.service.MetricService;

@RestController
@RequestMapping("/api/metrics")
public class MetricController {
    private final MeterRegistry meterRegistry;
    private final MetricService metricService;

    public MetricController(MeterRegistry meterRegistry, MetricService metricService) {
        this.meterRegistry = meterRegistry;
        this.metricService = metricService;
    }

    @PostMapping("/count/visit")
    public ResponseEntity<String> viewUserPage() {
        meterRegistry.counter("user.page.view").increment();
        return ResponseEntity.ok("View recorded successfully");
    }

    @GetMapping("/count/books")
    public ResponseEntity<Long> getBookCount() {
        long bookCount = metricService.getBookCount();
        return ResponseEntity.ok(bookCount);
    }

    @GetMapping("/count/categories")
    public ResponseEntity<Long> getCategoryCount() {
        long categoryCount = metricService.getCategoryCount();
        return ResponseEntity.ok(categoryCount);
    }

    @GetMapping("/count/authors")
    public ResponseEntity<Long> getAuthorCount() {
        long authorCount = metricService.getAuthorCount();
        return ResponseEntity.ok(authorCount);
    }

    @GetMapping("/count/publishers")
    public ResponseEntity<Long> getPublisherCount() {
        long publisherCount = metricService.getPublisherCount();
        return ResponseEntity.ok(publisherCount);
    }

    @GetMapping("/count/users")
    public ResponseEntity<Long> getUserCount() {
        long userCount = metricService.getUserCount();
        return ResponseEntity.ok(userCount);
    }

    @GetMapping("/count/loans")
    public ResponseEntity<Long> getLoanCount() {
        long loanCount = metricService.getLoanCount();
        return ResponseEntity.ok(loanCount);
    }

    @GetMapping("/count/requests")
    public ResponseEntity<Long> getRequestCount() {
        long requestCount = metricService.getRequestCount();
        return ResponseEntity.ok(requestCount);
    }
}
