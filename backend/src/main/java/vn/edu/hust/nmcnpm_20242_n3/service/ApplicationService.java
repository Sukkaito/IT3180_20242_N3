package vn.edu.hust.nmcnpm_20242_n3.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vn.edu.hust.nmcnpm_20242_n3.Nmcnpm20242N3Application;
import vn.edu.hust.nmcnpm_20242_n3.entity.StatusLog;
import vn.edu.hust.nmcnpm_20242_n3.repository.StatusLogRepository;

import java.time.LocalDateTime;

@Service
public class ApplicationService {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationService.class);

    private final StatusLogRepository statusLogRepository;

    @Autowired
    public ApplicationService(StatusLogRepository statusLogRepository) {
        this.statusLogRepository = statusLogRepository;
    }

    @PreDestroy
    public void onApplicationShutdown() {
        logger.info("Application is shutting down at: {}", LocalDateTime.now());

        // Save a final status log indicating shutdown
        StatusLog log = new StatusLog("server", "NOT OK", LocalDateTime.now(), "Application shutting down");
        statusLogRepository.save(log);
    }

    public void onApplicationStartup() {
        logger.info("Application is starting up at: {}", LocalDateTime.now());

        // Save a startup status log
        StatusLog log = new StatusLog("server", "OK", LocalDateTime.now(),
                "Application started");
        statusLogRepository.save(log);
    }

    public void restart() {
        logger.info("Application restart requested at: {}", LocalDateTime.now());

        // Save a restart status log
        StatusLog log = new StatusLog("server", "OK", LocalDateTime.now(),
                "Application restarting");
        statusLogRepository.save(log);

        // Logic to restart the application
        // This can be done by calling the main method or using Spring's ApplicationContext
        Nmcnpm20242N3Application.restart();
    }

    public void shutdown() {
        logger.info("Application shutdown requested at: {}", LocalDateTime.now());

        // Save a shutdown status log
        StatusLog log = new StatusLog("server", "NOT OK", LocalDateTime.now(), "Application shutdown requested");
        statusLogRepository.save(log);

        // Schedule shutdown after a brief delay to allow response to be sent
        Thread shutdownThread = new Thread(() -> {
            try {
                logger.info("Executing shutdown in 1 second...");
                Thread.sleep(1000);
                // Exit the application with status code 0 (successful exit)
                System.exit(0);
            } catch (InterruptedException e) {
                logger.error("Shutdown interrupted: {}", e.getMessage());
                Thread.currentThread().interrupt();
            }
        });

        shutdownThread.setDaemon(false);
        shutdownThread.start();
    }
}