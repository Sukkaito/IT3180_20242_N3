package vn.edu.hust.nmcnpm_20242_n3.repository;


import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookCopy;

import java.util.List;
import java.util.Optional;

@Repository
@NonNullApi
public interface BookCopyRepository extends JpaRepository<BookCopy, String> {
    boolean existsById(String id);
    List<BookCopy> findByStatus(String status);
    Optional<BookCopy> findById(String userId);

    boolean existsByBookCopyIdAndStatus(String bookCopyId, String status);

}
