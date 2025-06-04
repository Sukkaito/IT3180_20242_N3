package vn.edu.hust.nmcnpm_20242_n3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.hust.nmcnpm_20242_n3.entity.Fine;

import java.util.Date;
import java.util.List;

@Repository
public interface FineRepository extends JpaRepository<Fine, String> {
    @Query("SELECT f FROM Fine f WHERE f.bookLoan.user.id = :userId")
    List<Fine> findByUserId(@Param("userId") String userId);

    List<Fine> findByBookLoanId(String bookLoanId);

    List<Fine> findByAmountGreaterThan(double amount);

    List<Fine> findByCreatedAtBetween(Date startDate, Date endDate);
}