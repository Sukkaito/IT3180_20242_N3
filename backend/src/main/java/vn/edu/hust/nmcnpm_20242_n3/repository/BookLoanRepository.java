package vn.edu.hust.nmcnpm_20242_n3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import vn.edu.hust.nmcnpm_20242_n3.entity.Book;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookCopy;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookLoan;
import org.springframework.stereotype.Repository;
import vn.edu.hust.nmcnpm_20242_n3.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookLoanRepository extends CrudRepository<BookLoan, Long> {
    boolean existsByUserIdAndBookCopyId(String userId, String bookCopyId) ;
    BookLoan findByUserId(String userId);
    void deleteByUserId(String userId);


    @Query("SELECT bl.user FROM BookLoan bl WHERE bl.bookCopy.id = ?1")
    List<User> getUserListByBookCopyId(String bookCopyId);

    @Query("SELECT bl.bookCopy.originalBook FROM BookLoan bl WHERE bl.user.id = ?1 AND bl.status = 'BORROWED'")
    List<Book> findBorrowedBooksByUserId(String userId);

}

