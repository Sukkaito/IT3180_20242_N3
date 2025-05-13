package vn.edu.hust.nmcnpm_20242_n3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookCopy;

import java.util.List;

@Repository
public interface BookCopyRepository extends JpaRepository<BookCopy, String> {
    List<BookCopy> findByOriginalBook_BookIdAndStatus(int bookId, vn.edu.hust.nmcnpm_20242_n3.constant.BookCopyStatusEnum status);
}
