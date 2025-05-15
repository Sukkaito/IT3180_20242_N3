package vn.edu.hust.nmcnpm_20242_n3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookLoan;

import java.util.List;

@Repository
public interface BookLoanRepository extends JpaRepository <BookLoan, Integer> {
    @Query("SELECT bl FROM BookLoan bl WHERE bl.user.id = :userId")
    List<BookLoan> findByUserId(@Param("userId") int userId);
}
