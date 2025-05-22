package vn.edu.hust.nmcnpm_20242_n3.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookCopyStatusEnum;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookCopy;

import java.util.Optional;

@Repository
public interface BookCopyRepository extends CrudRepository<BookCopy, Integer> {
    Optional<BookCopy> findFirstByOriginalBook_BookIdAndStatus(Integer bookId, BookCopyStatusEnum status);
    Optional<BookCopy> findById(int id);
}
