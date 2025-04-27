package vn.edu.hust.nmcnpm_20242_n3.repository;


import org.springframework.data.repository.CrudRepository;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookLoan;

public interface BookLoanRepository extends CrudRepository<BookLoan, String> {
}
