package vn.edu.hust.nmcnpm_20242_n3.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.edu.hust.nmcnpm_20242_n3.entity.Staff;

import java.util.Optional;

@Repository
public interface StaffRepository extends CrudRepository<Staff, String> {
    Optional<Staff> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<Staff> findById(String id);
    void deleteById(String id);
}