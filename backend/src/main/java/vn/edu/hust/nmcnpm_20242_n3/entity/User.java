package vn.edu.hust.nmcnpm_20242_n3.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import vn.edu.hust.nmcnpm_20242_n3.constant.BookLoanStatusEnum;
import vn.edu.hust.nmcnpm_20242_n3.constant.UserStatusEnum;

import java.util.Date;

// Needed for HTTP requests and responses
@Setter
@Getter


@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @Column(unique = true, nullable = false)
    String name;
    @Column
    String userName;
    @Column(unique = true)
    String email;
    @Column(nullable = false)
    String password;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    Date CreatedAt;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    Date UpdatedAt;
    @ManyToOne
    Role role;
    @Enumerated(EnumType.STRING)
    UserStatusEnum status;
    @PrePersist
    protected void onCreate() {
        CreatedAt = new Date();
        UpdatedAt = new Date();
        status = UserStatusEnum.FREE;
    }

    @PreUpdate
    protected void onUpdate() {
        UpdatedAt = new Date();
    }
}

