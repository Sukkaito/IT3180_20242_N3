package vn.edu.hust.nmcnpm_20242_n3.repository;

import org.hibernate.annotations.processing.SQL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookLoan;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import vn.edu.hust.nmcnpm_20242_n3.entity.User;

import java.util.List;

@Repository
public interface BookLoanRepository extends JpaRepository<BookLoan, Long> {
    boolean existsByUserIdAndBookCopyId(String userId, String bookCopyId) ;
    BookLoan findByUserId(String userId);
    void deleteByUserId(String userId);

    @Query("SELECT bl.user FROM BookLoan bl WHERE bl.bookCopy.id = ?1")
    List<User> getUserListByBookCopyId(String bookCopyId);
}

