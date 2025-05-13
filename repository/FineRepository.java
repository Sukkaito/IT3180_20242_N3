package vn.edu.hust.nmcnpm_20242_n3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.hust.nmcnpm_20242_n3.entity.Fine;

import java.util.Date;
import java.util.List;

@Repository
public interface FineRepository extends JpaRepository<Fine, String> {

    List<Fine> findByUser_Id(String userId);

    List<Fine> findByBookLoan_Id(String bookLoanId);

    List<Fine> findByAmountGreaterThan(double amount);

    List<Fine> findByCreatedAtBetween(Date startDate, Date endDate);
}
