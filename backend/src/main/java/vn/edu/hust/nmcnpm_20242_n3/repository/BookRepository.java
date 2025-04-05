package vn.edu.hust.nmcnpm_20242_n3.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.hust.nmcnpm_20242_n3.entity.Book;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends CrudRepository<Book, Integer> {
    Optional<Book> findByTitle(String title);
    void deleteByTitle(String title);

    @Query("SELECT b FROM Book b WHERE b.title LIKE %:title%")
    List<Book> searchByTitle(@Param("title") String title);

    @Query("SELECT b FROM Book b JOIN b.publisher p WHERE p.name LIKE %:publisherName%")
    List<Book> searchByPublisherName(@Param("publisherName") String publisherName);

    @Query("SELECT b FROM Book b JOIN b.categories c WHERE c.name LIKE %:categoryName%")
    List<Book> searchByCategoryName(@Param("categoryName") String categoryName);

    @Query("SELECT b FROM Book b JOIN b.authors a WHERE a.name LIKE %:authorName%")
    List<Book> searchByAuthorName(@Param("authorName") String authorName);
}