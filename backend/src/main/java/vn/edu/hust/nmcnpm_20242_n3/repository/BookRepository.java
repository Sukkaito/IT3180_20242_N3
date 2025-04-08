package vn.edu.hust.nmcnpm_20242_n3.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.hust.nmcnpm_20242_n3.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends CrudRepository<Book, Integer> {
    Optional<Book> findByTitle(String title);
    Optional<Book> findById(int id);

    void deleteById(int id);
    Page<Book> findAll(Pageable pageable);

    @Query("SELECT b FROM Book b WHERE b.title LIKE %:title%")
    List<Book> searchByTitle(@Param("title") String title);

    @Query("SELECT b FROM Book b JOIN b.publisher p WHERE p.id = :publisherId")
    List<Book> searchByPublisherId(@Param("publisherId") Integer publisherId);

    @Query("SELECT b FROM Book b JOIN b.categories c WHERE c.id = :categoryId")
    List<Book> searchByCategoryId(@Param("categoryId") Integer categoryId);

    @Query("SELECT b FROM Book b JOIN b.authors a WHERE a.id = :authorId")
    List<Book> searchByAuthorId(@Param("authorId") Integer authorId);
}
