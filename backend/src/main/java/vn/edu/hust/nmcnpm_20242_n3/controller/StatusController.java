package vn.edu.hust.nmcnpm_20242_n3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.hust.nmcnpm_20242_n3.service.StatusService;

import java.time.LocalDateTime;
import java.util.Map;

@Controller
@RequestMapping("/api/status")
public class StatusController {

    private final StatusService statusService;

    @Autowired
    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    @GetMapping
    public ResponseEntity<Map<String, String>> getStatus() {
        try {
            return ResponseEntity.ok(statusService.checkStatus());
        } catch (Exception e) {
                return ResponseEntity.status(500).body(Map.of("error", "Internal Server Error"));
        }
    }

    @GetMapping("/logs")
    public ResponseEntity<?> getStatusLogs(@RequestParam(value = "dayCount", defaultValue = "30") int days) {
        try {
            return ResponseEntity.ok(
                    statusService.getLogsByTimeRange(
                            LocalDateTime.now().minusDays(days), LocalDateTime.now())
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Internal Server Error"));
        }
    }

    @GetMapping("/logs/{component}")
    public ResponseEntity<?> getStatusLogsByComponent(
            @PathVariable String component,
            @RequestParam(value = "dayCount", defaultValue = "30") int days) {
        try {
            return ResponseEntity.ok(
                    statusService.getLogsByComponentAndTimeRange(
                            component, LocalDateTime.now().minusDays(days), LocalDateTime.now())
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Internal Server Error"));
        }
    }
}
