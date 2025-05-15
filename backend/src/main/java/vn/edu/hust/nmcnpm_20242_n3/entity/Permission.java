package vn.edu.hust.nmcnpm_20242_n3.entity;
import lombok.Getter;
import jakarta.persistence.*;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "permissions")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(unique = true, nullable = false)
    String name;
    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles;
}
