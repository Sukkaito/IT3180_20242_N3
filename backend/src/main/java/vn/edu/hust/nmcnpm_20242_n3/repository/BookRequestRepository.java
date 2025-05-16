package vn.edu.hust.nmcnpm_20242_n3.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookRequest;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRequestRepository extends CrudRepository<BookRequest, String> {
    List<BookRequest> findAll();
    Optional<BookRequest> findById(String id);
}