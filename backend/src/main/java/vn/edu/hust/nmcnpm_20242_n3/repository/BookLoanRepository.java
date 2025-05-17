package vn.edu.hust.nmcnpm_20242_n3.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

import vn.edu.hust.nmcnpm_20242_n3.constant.BookLoanStatusEnum;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookLoan;

@Repository
public interface BookLoanRepository extends CrudRepository<BookLoan, String> {
    @Query("SELECT b FROM BookLoan b WHERE b.status = :status")
    List<BookLoan> findByStatus(@Param("status") BookLoanStatusEnum status);

    @Query("SELECT b FROM BookLoan b WHERE b.bookCopy.id=:bookCopyId")
    Optional<BookLoan> findByBookCopyId(@Param("bookCopyId") Integer bookCopyId);

    @Query("SELECT b FROM BookLoan b WHERE b.user.id=:userId")
    List<BookLoan> findAllByUserId(@Param("userId") String userId);

    @Query("SELECT b FROM BookLoan b WHERE b.user.id=:userId AND b.status = :status AND b.bookCopy.id=:bookCopyId")
    Optional<BookLoan> findByBookCopyIdAndUserIdAndStatus(@Param("userId") String userId,
            @Param("bookCopyId") Integer bookCopyId,
            @Param("status") BookLoanStatusEnum status);
}
