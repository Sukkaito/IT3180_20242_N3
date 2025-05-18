package vn.edu.hust.nmcnpm_20242_n3.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "status_logs")
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class StatusLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NonNull
    private String component;      // "db", "server", etc.

    @Column(nullable = false)
    @NonNull
    private String status;         // "OK", "ERROR", "NOT OK"

    @Column(nullable = false)
    @NonNull
    private LocalDateTime timestamp;

    @NonNull
    private String message;        // Optional details about the status change
}