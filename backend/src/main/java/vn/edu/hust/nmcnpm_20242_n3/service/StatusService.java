package vn.edu.hust.nmcnpm_20242_n3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vn.edu.hust.nmcnpm_20242_n3.entity.StatusLog;
import vn.edu.hust.nmcnpm_20242_n3.repository.StatusLogRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatusService {

    private final DataSource dataSource;
    private final StatusLogRepository statusLogRepository;

    private Map<String, String> lastStatus = new HashMap<>();

    @Autowired
    public StatusService(DataSource dataSource, StatusLogRepository statusLogRepository) {
        this.dataSource = dataSource;
        this.statusLogRepository = statusLogRepository;
    }

    @Scheduled(fixedRate = 300000)
    public Map<String, String> checkStatus() {
        String dbStatus = getDatabaseStatus();
        String serverStatus = "OK"; // Replace with actual server status check

        Map<String, String> currentStatus = Map.of(
                "database", dbStatus,
                "server", serverStatus
        );

        // Log status changes if any
        logStatusChanges(currentStatus);

        return currentStatus;
    }

    public String getDatabaseStatus() {
        try (Connection connection = dataSource.getConnection()) {
            if (connection != null && connection.isValid(5)) {
                return "OK";
            } else {
                return "UNAVAILABLE";
            }
        } catch (SQLException e) {
            return "ERROR";
        }
    }

    private void logStatusChanges(Map<String, String> currentStatus) {
        for (Map.Entry<String, String> entry : currentStatus.entrySet()) {
            String component = entry.getKey();
            String status = entry.getValue();

            // If this is a new component or status has changed
            if (!lastStatus.containsKey(component) || !lastStatus.get(component).equals(status)) {
                String message = lastStatus.containsKey(component) ?
                        "Status changed from " + lastStatus.get(component) + " to " + status :
                        "Initial status check";

                StatusLog log = new StatusLog(component, status, LocalDateTime.now(), message);
                statusLogRepository.save(log);

                // Update last status
                lastStatus.put(component, status);
            }
        }
    }

    // Methods to retrieve status logs
    public List<StatusLog> getRecentLogs() {
        return statusLogRepository.findTop10ByOrderByTimestampDesc();
    }

    public List<StatusLog> getLogsByComponent(String component) {
        return statusLogRepository.findByComponentOrderByTimestampDesc(component);
    }

    public List<StatusLog> getLogsByTimeRange(LocalDateTime start, LocalDateTime end) {
        return statusLogRepository.findByTimestampBetweenOrderByTimestampDesc(start, end);
    }

    public List<StatusLog> getLogsByComponentAndTimeRange(String component, LocalDateTime start, LocalDateTime end) {
        return statusLogRepository.findByComponentAndTimestampBetweenOrderByTimestampDesc(component, start, end);
    }
}