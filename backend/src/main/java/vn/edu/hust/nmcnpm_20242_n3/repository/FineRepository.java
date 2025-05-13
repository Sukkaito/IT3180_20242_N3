package vn.edu.hust.nmcnpm_20242_n3.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.edu.hust.nmcnpm_20242_n3.entity.Fine;

import java.util.Date;
import java.util.List;

@Repository
public interface FineRepository extends CrudRepository<Fine, String> {
    List<Fine> findByUserId(String userId);

    List<Fine> findByBookLoanId(String bookLoanId);

    List<Fine> findByAmountGreaterThan(double amount);

    List<Fine> findByCreatedAtBetween(Date startDate, Date endDate);
}