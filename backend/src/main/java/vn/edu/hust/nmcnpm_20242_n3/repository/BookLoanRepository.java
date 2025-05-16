package vn.edu.hust.nmcnpm_20242_n3.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookLoan;

import java.util.Optional;

@Repository
public interface BookLoanRepository extends CrudRepository<BookLoan, String> {
    Optional<BookLoan> findByCurrentBookRequestId(String requestId);
}